function result() {
    var cantWalkInStr = "Nie przejdziesz. Drzwi są zamknięte na klucz. Po zamku widać, że raczej nikt tam dłuższy czas nie zaglądał. Jest lekko zardzewiały bez świeżych śladów użytkowania.";
    var successfullWalInStr = "Drzwi są zamknięte na klucz, ale pamiętasz, że nieopodal znalazłeś mały zardzewiały kluczyk, który może pasować. Wsadzasz klucz do zamka, z trudem przekręcasz i... Tak! Udało się! Mechanizm ustąpił cicho trzaskając. Drzwi są otwarte.";

    var isKeyInEquipment = player.getEquipment().stream()
        .filter(function (itemStack) {
            return itemStack.getItemId() === "7"; //Zardzewialy klucz
        })
        .findFirst()
        .isPresent();

    if (isKeyInEquipment) {
        response.getPlayerEvents()
            .add(successfullWalInStr);
    } else {
        response.getPlayerEvents()
            .add(cantWalkInStr);
    }

    return isKeyInEquipment;
}
result();