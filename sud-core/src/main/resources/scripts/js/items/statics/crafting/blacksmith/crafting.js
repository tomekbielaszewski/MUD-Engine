//@ sourceURL=sud-core/src/main/resources/scripts/js/items/statics/crafting/blacksmith/crafting.js
//line above is for IntelliJ debugging purposes
loadScript("105-materials");
loadScript("105-recipes");

function itemIsOnRecipesList(item) {
    for (i = 0; i < recipes.length; i++) {
        if (recipes[i].name === item) {
            return true;
        }
    }
    return false;
}

function getRecipe(item) {
    for (i = 0; i < recipes.length; i++) {
        if (recipes[i].name === item) {
            return recipes[i];
        }
    }
}

function playerIsAbleToCraft(recipe, amountOfItems) {
    var hasAllIngredients = true;

    for (i = 0; i < recipe.ingredients.length; i++) {
        var itemId = recipe.ingredients[i].id;
        var amount = recipe.ingredients[i].amount;

        if (!playerHas(itemId, amount * amountOfItems)) {
            hasAllIngredients = false;
        }
    }
    return hasAllIngredients; //item has no ingredients, so it can be crafted without check
}

function takeMaterialsFromPlayer(recipe, amountOfItems) {
    for (i = 0; i < recipe.ingredients.length; i++) {
        var itemId = recipe.ingredients[i].id;
        var amount = recipe.ingredients[i].amount;
        var itemName = itemRepo.get(itemId).getName();

        equipmentService.removeItems(itemName, amount * amountOfItems, player, response);
    }
}

function giveCraftedItem(recipe, amount) {
    equipmentService.addItems(recipe.name, amount, player, response);
}

function informAboutSuccessfulCrafting(recipe, amount) {
    tellPlayer("Udało Ci się stworzyć " + recipe.name + " w ilości " + amount + " szt. Przedmiot znajdziesz w swoim ekwipunku")
}

function informAboutInsufficientMaterials(recipe, amount) {
    tellPlayer("Masz za mało materiałów!");
}

function informThatItemIsUnknown(item) {
    tellPlayer(item + "? Nie wiem jak stworzyć taki przedmiot...");
}

function craft(item, amount) {
    if (amount <= 0) {
        tellPlayer("Jak chcesz stworzyć taką liczbę przedmiotów?!")
    } else {
        if (itemIsOnRecipesList(item)) {
            var recipe = getRecipe(item);

            if (playerIsAbleToCraft(recipe, amount)) {
                takeMaterialsFromPlayer(recipe, amount);
                giveCraftedItem(recipe, amount);
                informAboutSuccessfulCrafting(recipe, amount);
            } else {
                informAboutInsufficientMaterials(recipe, amount);
            }
        } else {
            informThatItemIsUnknown(item);
        }
    }
}

interpretCommand(function (commandSplit) {
    var itemName = commandSplit[0];
    var amount = commandSplit.length > 2 ? commandSplit[1] : 1;

    logger.info("{} is crafting {}x {}", [player.getName(), amount, itemName]);
    craft(itemName, amount);
});