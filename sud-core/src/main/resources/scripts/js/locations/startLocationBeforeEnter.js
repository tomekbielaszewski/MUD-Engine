function result() {
    var cantWalkInStr = "Nie przejdziesz. Drzwi są zamknięte...";
    response.getPlayerEvents()
        .add(cantWalkInStr);

    return false;
}
result();