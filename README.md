# SUD [![Build Status](https://travis-ci.org/tomekbielaszewski/SUD.svg)](https://travis-ci.org/tomekbielaszewski/SUD)


# Dokumentacja

# Spis treści
1. [Wstęp](#wstęp)
2. [Lokalizacje plikow gry](#lokalizacje-plikow-gry)
3. [Komendy](#komendy)
4. [Gracz](#gracz)
5. [Skrypty](#skrypty)
6. [Przedmioty](#przedmioty)
7. [Lokacje](#lokacje)
8. [Teksty powiadomień](#teksty-powiadomień)

## Wstęp

## Lokalizacje plikow gry
Lokalizacje plikow gry domyslnie znajdują się pod:
- Lokacje w folderze `locations`
- Przedmioty w folderze `items`
- Przeciwnicy w folderze `mobs`
- Skrypty w folderze `scripts`

Przy czym sciezki do folderów można modyfikować przez zmianę ścieżek do nich w pliku [`assets.properties`](sud-core/src/main/resources/assets.properties), gdzie:
- klucz `assets.json.path.locations` to ścieżka do folderu lokacji
- klucz `assets.json.path.items` to ścieżka do folderu przedmiotów
- klucz `assets.json.path.mobs` to ścieżka do folderu przeciwników
- klucz `assets.json.path.scripts` to ścieżka do folderu skryptów

## Komendy
Jedyny sposób interakcji ze światem odbywa się przez komendy wywoływane przez gracza. Silnik posiada zaimplementowane podstawowe komendy jak i pozwala na implementację własnych - powiązanych z przedmiotami.

### Podstawowe komendy
Podstawowymi komendami w silniku gry są:
- Przechodzenie po lokacjach
  - Na północ - `org.grizz.game.commands.impl.movement.MoveNorthCommand`
  - Na południe - `org.grizz.game.commands.impl.movement.MoveSouthCommand`
  - Na wschód - `org.grizz.game.commands.impl.movement.MoveEastCommand`
  - Na zachód - `org.grizz.game.commands.impl.movement.MoveWestCommand`
  - W górę - `org.grizz.game.commands.impl.movement.MoveUpCommand`
  - W dół - `org.grizz.game.commands.impl.movement.MoveDownCommand`
- Rozglądanie się - `org.grizz.game.commands.impl.LookAroundCommand`
- Wyrzucanie przedmiotów - `org.grizz.game.commands.impl.DropCommand`
- Podnoszenie przedmiotów - `org.grizz.game.commands.impl.PickUpCommand`
- Podgląd ekwipunku - `org.grizz.game.commands.impl.ShowEquipmentCommand`

### Mapowanie komend
Podstawowe komendy są zmapowane do słów pozwalających na wywołanie ich. Takie mapowanie znajduje się w pliku [`command-mapping.properties`](sud-core/src/main/resources/command-mapping.properties). W pliku tym znajdziemy pary klucz wartośc gdzie:
- kluczem jest - pełna nazwa klasy podstawowej komendy
- wartością są - słowa zmapowane do tej komendy. Może być ich wiele, muszą wtedy być oddzielone przecinkami. Słowa nie mogą zawierać polskich znaków ani innych liter zawierających 'akcenty'. Spacje przed i po także nie są dozwolone. W przypadku komendy potrzebującej pewnych wartości wejściowych należy wskazać miejsce podawanych danych:
  - `([\\D]+)` - dla danych słownych
  - `([\\d]+)` - dla danych liczbowych
Przy czym **kolejność parametrów** komend jest **ważna**

### Parametry komend
Następujące komendy wymagają podania parametrów wejściowych:
- `PickUpCommand` - parametry:
  - `([\\D]+)` - Nazwa przedmiotu, który chcesz podnieść i umieścić w ekwipunku
  - `([\\d]+)` - [Opcjonalny, wartość domyślna = 1] Liczba przedmiotów, które chcesz podnieść i umieścić w ekwipunku
- `DropCommand` - parametry:
  - `([\\D]+)` - Nazwa przedmiotu, który chcesz wyrzucić i umieścić na bierzącej lokacji
  - `([\\d]+)` - [Opcjonalny, wartość domyślna = 1] Liczba przedmiotów, które chcesz wyrzucić i umieścić na bierzącej lokacji

## Gracz
### Statystyki/karta gracza
### Parametry
### Ekwipunek
### Walka

## Skrypty
### Model skryptów i mapowanie skryptów
Modelem skryptów jest plik JSON znajdujący się w folderze [`assets.json.path.scripts`](sud-core/src/main/resources/scripts/):
```javascript
[
  {
    "id": "100",
    "name": "Always true script",
    "path": "scripts/js/alwaysTrue.js"
  }
]
```

Gdzie:
- `id` jest identyfikatorem używanym do odniesienia się do konkretnego skryptu w plikach przedmiotów, lokacji itp
- `name` opisuje do czego służy skrypt - ma to zastosowanie czysto dokumentacyjne
- `path` to ścieżka do pliku javascript

W pliku pod ścieżką podaną w zmiennej `path` znajduje się właściwy kod skryptu.

### Silnik JavaScript - Nashorn
Skrypty odpalane są za pomocą silnika JavaScript Nashorn. Znaczy to, że można w skryptach używać wszystkiego na co silnik ten pozwala włącznie z obiektami Java.

### Podstawowe zmienne
W każdym odpalonym skrypcie jest dostęp do następujących zmiennych podstawowych:
- `player` jest typu org.grizz.game.model. **PlayerContext** - pozwala na dostęp do wszystkich statystyk gracza, jego ekwipunku, parametrów, obecnej lokalizacji,
- `response` jest typu org.grizz.game.model. **PlayerResponse** - udostępnia obiekt odpowiedzi
- `command` jest typu java.lang. **String** - wartością jest wywołana przez gracza komenda

### Dostęp do serwisów
Zmienne pod którymi dostępne sa serwisy ułatwiające interakcję ze światem gry:
- `locationRepo` jest typu org.grizz.game.model.repository. **LocationRepo** - pozwala na pobieranie lokalizacji na podstawie ID
//TODO: uzupełnić listę serwisów

### Zwracanie wartości
Wartość jaką zwróci skrypt jest wartością zwróconą przez ostatnią funkcję w skrypcie. Przykładową konstrukcją zwracającą zawsze wartość true jest:
```javascript
function alwaysTrue() {
    // here goes all the script instructions
    return true;
}
alwaysTrue();
```

## Przedmioty
### Model przedmiotów
### Komendy, mapowanie i skryptowanie komend
### Rodzaje przedmiotów

## Lokacje
### Model lokacji
### Skryptowanie lokacji
### Przedmioty statyczne

## Teksty powiadomień
### Klucze wiadomości
