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
### pliki konfiguracyjne skryptów i mapowanie skryptów
Plikiem konfiguracyjnym skryptów jest plik JSON znajdujący się w folderze domyślnym [`scripts`](sud-core/src/main/resources/scripts/) [[*]](#lokalizacje-plikow-gry):
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
Przystepne materiały można znaleźć [tutaj](https://docs.oracle.com/javase/8/docs/technotes/guides/scripting/nashorn/api.html) i [tutaj](http://winterbe.com/posts/2014/04/05/java8-nashorn-tutorial/)

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
Pliki JSON opisujące właściwości przedmiotów znajdują się w folderze domyślnym [`items`](sud-core/src/main/resources/items/) [[*]](#lokalizacje-plikow-gry). Przykładowy wpis w pliku:
```javascript
[
  {
    "id": "1",
    "name": "Złota moneta",
    "description": "Sporawy, złoty krążek z wizerunkiem jednego z dawno juz nie żyjących władców.",
    "itemType": "MISC",
    "commands": [
      {
        "command": "dzyn-dzyn",
        "scriptId": "101"
      }
    ]
  }
]
```

Gdzie:
- `id` jest unikatowym identyfikatorem pliku. ID nie może się powtarzać na przestrzeni wszystkich plików JSON opisujących właściwości przedmiotów.
- `name` jest to nazwa przedmiotu widoczna dla gracza. Gracz także będzie się odnosił do przedmiotu poprzez tą właśnie nazwę
- `description` jest to szczegółowy opis przedmiotu. Może być widoczny podczas podglądu ekwipunku lub przedmiotów na lokacji
- `itemType` jest to jedna z wartości [`WEAPON`,`ARMOR`,`MISC`,`STATIC`]. Dokładniejszy opis [poniżej](#rodzaje-przedmiotów)
- `commands` jest to mapowanie komend możliwych do wywołania na danym przedmiocie. Komenda nie może być taka sama jak jedna z [wbudowanych komend](#komendy), a w przypadku zduplikowania komendy na przestrzeni różnych przedmiotów i posiadanie tych dwóch różnych przedmiotów w ekwipunku nie gwarantuje wywołania komendy na własciwym przedmiocie.
  - `command` jest to słowo/a wywołujące komendę. Możliwe jest używanie parametrów jak w przypadku [zwykłego mapowania](#mapowanie-komend) skryptów, jednak wtedy skrypt musi obsługiwać parsowanie komendy w celu pozyskania parametrów wejściowych.
  - `scriptID` jest to ID [skryptu](#skrypty)
  
W przypadku przedmiotów innych typów niż `MISC` dostępne są także dodatkowe zmienne. O tym [poniżej](#rodzaje-przedmiotów)
### Komendy, mapowanie i skryptowanie komend
Jak opisano wyżej - przedmioty można oskryptować, za pomocą zmapowania skryptu w pliku konfiguracyjnym JSON. W przypadku zmapowania komendy przedmiotu do nieistniejącego skryptu - zostanie wyrzucony wyjątek podczas uruchamiania gry. Jednak jeśli zostanie zmapowanych kilka identycznych komend do różnych skryptów - nie jest gwarantowane który skrypt zostanie uruchomiony.
Skrypty uruchamiane na przedmiotach nie oczekują zwrócenia żadnej wartości, także [taka](#zwracanie-wartości) konstrukcja nie ma w tym przypadku zastosowania.
### Rodzaje przedmiotów
Istnieją obecnie 4 typy przedmiotów - [`WEAPON`,`ARMOR`,`MISC`,`STATIC`]. Niektóre typy, oprócz zmiennych podstawowych [`id`,`name`,`description`,`itemType`,`commands`], posiadają także dodatkowe zmienne:
- `WEAPON` przedmioty opisujące broń możliwą do trzymania w rękach:
  - `weaponType` jest to jedna z wartości określających typ broni:
    - `SWORD1H` - Miecze jednoręczne
    - `SWORD2H` - Miecze dwuręczne
    - `DAGGER` - Sztylety - broń jednoręczna
    - `BOW` - Luki - broń dwuręczna, strzelająca na odległość
    - `CROSSBOW` - Kusze - broń dwuręczna, strzelająca na odległość
    - `AXE1H` - Topory jednoręczne
    - `AXE2H` - Topory dwuręczne
    - `HAMMER` - Młoty bojowe - dwuręczne
    - `POLEARM` - Broń drzewcowa - dwuręczna
    - `STAFF` - Laski bojowe - dwuręczne
    - `THROWN` - Broń rzucana - jednoręczna, zadająca obrażenia na odległość
  - `minDamage` są to minimalne obrażenia
  - `maxDamage` są to maksymalne obrażenia
- `ARMOR` przedmioty opisujące elementy zbroi, możliwe do założenia na siebie:
  - //TODO: uzupełnic po zaimplementowaniu zbroi
- `MISC` różne przedmioty niekwalifikujące się do powyższych typów, takie jak: monety, drobiazgi, przedmioty questowe etc
- `STATIC` specjalny typ przedmiotów, których gracz nie może podnieść ani mieć w ekwipunku. Są to elementy interaktywne lokacji, takie jak: ołtarze, studnie, kowadła, dźwignie etc. Sens istnienia tych przedmiotów jest posiadanie zmapowanych komend
  
## Lokacje
### Model lokacji
### Skryptowanie lokacji
### Przedmioty statyczne

## Teksty powiadomień
### Klucze wiadomości
