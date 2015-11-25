//@ sourceURL=sud-core/src/main/resources/scripts/js/locations/startLocationBeforeEnter.js
//line above is for IntelliJ debugging purposes

function result() {
    var cantWalkInStr = "Nie przejdziesz. Drzwi są zamknięte...";
    response.getPlayerEvents()
        .add(cantWalkInStr);

    return false;
}
result();