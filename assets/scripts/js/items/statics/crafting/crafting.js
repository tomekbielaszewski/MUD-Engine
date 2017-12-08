//@ sourceURL=assets/scripts/js/items/statics/crafting/crafting.js
//line above is for IntelliJ debugging purposes

game.crafting = (function () {
  var titleRowPattern = "%1$s\n------------";
  var itemDescRowPattern = "[%1$-20s]: \"%2$s\""; //Krótki miecz: Krótkie, lekko uszczerbione, jednoręczne ostrze. Prosty jelec. Zużyta rękojeść. Nic szczególnego.
  var ingredientRowPattern = "     %1$-3s %2$s";    //    2x Sztabka żelaza

  function itemIsOnRecipesList(item) {
    for (var i = 0; i < recipes.length; i++) {
      var recipe = recipes[i];
      if (recipe.name === item) {
        return true;
      }
    }
    return false;
  }

  function getRecipe(item) {
    for (var i = 0; i < recipes.length; i++) {
      if (recipes[i].name === item) {
        return recipes[i];
      }
    }
  }

  function playerHasMaterials(recipe, amountOfItems) {
    var hasAllIngredients = true;

    for (var i = 0; i < recipe.ingredients.length; i++) {
      var itemId = recipe.ingredients[i].id;
      var amount = recipe.ingredients[i].amount;

      if (!game.player.hasItems(itemId, amount * amountOfItems)) {
        hasAllIngredients = false;
      }
    }
    return hasAllIngredients; //item has no ingredients, so it can be crafted without check
  }

  function takeMaterialsFromPlayer(recipe, amountOfItems) {
    for (var i = 0; i < recipe.ingredients.length; i++) {
      var itemId = recipe.ingredients[i].id;
      var amount = recipe.ingredients[i].amount;

      game.player.removeItems(itemId, amount * amountOfItems);
    }
  }

  function giveCraftedItem(recipe, amount) {
    game.player.addItemsByName(recipe.name, amount);
  }

  function informAboutSuccessfulCrafting(recipe, amount) {
    game.player.message("Udało Ci się stworzyć " + recipe.name + " w ilości " + amount + "szt. Przedmiot znajdziesz w swoim ekwipunku");
  }

  function informAboutInsufficientMaterials() {
    game.player.message("Masz za mało materiałów!");
  }

  function informThatItemIsUnknown(item) {
    game.player.message(item + "? Nie wiem jak stworzyć taki przedmiot...");
  }

  function craft(item, amount) {
    if (amount <= 0) {
      game.player.message("Jak chcesz stworzyć taką liczbę przedmiotów?!")
    } else if (!doesPlayerHaveRequiredTools()) {
      informThatPlayerHasNoRequiredTools();
    } else {
      if (itemIsOnRecipesList(item)) {
        var recipe = getRecipe(item);

        if (playerHasMaterials(recipe, amount)) {
          takeMaterialsFromPlayer(recipe, amount);
          giveCraftedItem(recipe, amount);
          informAboutSuccessfulCrafting(recipe, amount);
        } else {
          informAboutInsufficientMaterials();
        }
      } else {
        informThatItemIsUnknown(item);
      }
    }
  }

  function printList(title, recipes) {

    game.player.message(game.formatText(titleRowPattern, title));

    for (var i = 0; i < recipes.length; i++) {
      var recipe = recipes[i];
      var product = game.item(recipe.id);

      game.player.message(game.formatText(itemDescRowPattern, product.getName(), product.getDescription()));

      for (var j = 0; j < recipe.ingredients.length; j++) {
        var ingredient = recipe.ingredients[j];
        var amount = ingredient.amount + "x";
        var material = game.item(ingredient.id);

        game.player.message(game.formatText(ingredientRowPattern, amount, material.getName()));
      }
    }
  }

  return {
    make: craft,
    list: printList
  }
})();
