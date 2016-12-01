# MUD-Engine [![Build Status](https://travis-ci.org/tomekbielaszewski/MUD-Engine.svg)](https://travis-ci.org/tomekbielaszewski/MUD-Engine)
<a href="https://zenhub.io"><img src="https://raw.githubusercontent.com/ZenHubIO/support/master/zenhub-badge.png"></a>


# Dokumentacja

# Spis treści
1. [Wstęp](#wstęp)
1. [Instalacja, konfiguracja i pierwsze uruchomienie](#instalacja-konfiguracja-i-pierwsze-uruchomienie)
1. [Lokalizacje plikow gry](#lokalizacje-plikow-gry)
1. [Komendy](#komendy)
1. [Gracz](#gracz)
1. [Skrypty](#skrypty)
1. [Przedmioty](#przedmioty)
1. [Lokacje](#lokacje)
1. [Teksty powiadomień](#teksty-powiadomień)
1. [Administrator](#administrator)

## Wstęp
MUD-Engine jak sama nazwa wskazuje jest silnikiem do tworzenia gier typu MUD, czyli Multi User Dungeon. Interakcja z grami tego typu odbywa się wyłącznie za pomocą komend i czytania opisów.
Silnik w całości napisany jest w Javie, natomiast skryptowany jest w JavaScripcie, a definicje przedmiotów, lokacji itp sa zapisane jako JSON. Zmienne elementy gry, takie jak model gracza czy przedmioty na lokacjach, zapisywane są w bazie MongoDB.

## Instalacja, konfiguracja i pierwsze uruchomienie
Silnik budowany jest z myślą o zachowaniu minimalnych zależności i minimalnej konfiguracji, więc do działania wymaga jedynie Javy 8 i MongoDB 3.

### Wymagana/zalecana konfiguracja to
#### Plik application.properties (opcjonalne)
Jest to plik szeroko opisany w dokumentacji Spring Boot. Zalecana konfiguracja to:
- logging.level.org.springframework=INFO
- logging.level.org.grizz.game=INFO
- logging.file=mud-engine.log
- banner.location=classpath:banner.txt
- spring.data.mongodb.database=mud-engine

#### Plik assets.properties (wymagane)
W tym pliku definiujemy lokalizacje główne dla wszystkich assetów (lokacje, itemy, skrypty)
Więcej info patrz -> [Lokalizacje plikow gry](#lokalizacje-plikow-gry)

#### Plik command-mapping.properties (wymagane)
W pliku znajduje się mapowanie komend do klas implementujących ich działanie. Tzw komendy systemowe.
Więcej info patrz -> [Komendy](#komendy)
 
#### Plik strings.properties (wymagane)
W pliku znajdują się wszystkie komunikaty występujące w silniku
Więcej info patrz -> [Teksty powiadomień](#teksty-powiadomień)

## Lokalizacje plikow gry
Lokalizacje plikow gry domyslnie znajdują się pod:
- Lokacje w folderze `locations`
- Przedmioty w folderze `items`
- Skrypty w folderze `scripts`
- Szablony odpowiedzi w folderze `response`

Przy czym sciezki do folderów można modyfikować przez zmianę ścieżek do nich w pliku [`assets.properties`](assets/assets.properties), gdzie:
- klucz `assets.json.path.locations` to ścieżka do folderu lokacji
- klucz `assets.json.path.items` to ścieżka do folderu przedmiotów
- klucz `assets.json.path.scripts` to ścieżka do folderu skryptów
- klucz `assets.response.templates.path` to ścieżka do folderu szablonów

## Komendy
Jedyny sposób interakcji ze światem odbywa się przez komendy wywoływane przez gracza. Silnik posiada zaimplementowane podstawowe komendy jak i pozwala na implementację własnych - powiązanych z przedmiotami.

### Wbudowane komendy
Komendy wbudowane dostępne dla wszystkich graczy:
- Przechodzenie po lokacjach
  - Na północ - `org.grizz.game.command.parsers.system.movement.NorthMoveCommand`
  - Na południe - `org.grizz.game.command.parsers.system.movement.SouthMoveCommand`
  - Na wschód - `org.grizz.game.command.parsers.system.movement.EastMoveCommand`
  - Na zachód - `org.grizz.game.command.parsers.system.movement.WestMoveCommand`
  - W górę - `org.grizz.game.command.parsers.system.movement.UpMoveCommand`
  - W dół - `org.grizz.game.command.parsers.system.movement.DownMoveCommand`
- Rozglądanie się - `org.grizz.game.command.parsers.system.LookAroundCommand`
- Wyrzucanie przedmiotów - `org.grizz.game.command.parsers.system.DropCommand`
- Podnoszenie przedmiotów - `org.grizz.game.command.parsers.system.PickUpCommand`
- Podgląd ekwipunku - `org.grizz.game.command.parsers.system.ShowEquipmentCommand`

Komendy wbudowane dostępne dla administratorów:
- Teleportacja graczy - `org.grizz.game.command.parsers.admin.AdminTeleportCommand`
- Tworzenie przedmiotów i przekazywanie ich graczom (tylko mobilne przedmioty) - `org.grizz.game.command.parsers.admin.AdminGiveItemCommand`
- Tworzenie przedmiotow i wyrzucanie ich na lokacji (statyczne i mobilne przedmioty) - `org.grizz.game.command.parsers.admin.AdminPutItemCommand`
- Wyświetlanie listy aktywnych graczy - `org.grizz.game.command.parsers.admin.AdminShowActivePlayerListCommand`

### Mapowanie komend
Wbudowane komendy są zmapowane do słów pozwalających na wywołanie ich. Takie mapowanie znajduje się w pliku [`command-mapping.properties`](assets/command-mapping.properties). W pliku tym znajdziemy pary klucz wartośc gdzie:
- kluczem jest - pełna nazwa klasy podstawowej komendy (wypisane wyżej)
- wartością są - słowa zmapowane do tej komendy. Może być ich wiele, muszą wtedy być oddzielone średnikami. Słowa nie mogą zawierać polskich znaków ani innych liter zawierających 'akcenty'. Spacje przed i po także nie są dozwolone (przed dopasowywanie wprowadzona przez uzytkownika komenda jest trimowana i usuwane są akcenty). W przypadku komendy potrzebującej pewnych wartości wejściowych należy opisac typ i nazwę wprowadzanego parametru:
  - `(?<word>[\\D]+)` - dla danych słownych
  - `(?<amount>[\\d]+)` - dla danych liczbowych
  - `(?<playerName>[\\w-]{4,})` - dla nicków
Gdzie `word`, `amount` oraz `playerName` to nazwy parametrów komendy

### Parametry komend
Komendy wymagające podania parametrów wejściowych:
- `PickUpCommand` - parametry (nazwa i typ):
  - `(?<itemName>[\\D]+)` - Nazwa przedmiotu, który chcesz podnieść i umieścić w ekwipunku
  - `(?<amount>[\\d]+)` - [Opcjonalny, wartość domyślna = 1] Liczba przedmiotów, które chcesz podnieść i umieścić w ekwipunku
- `DropCommand` - parametry (nazwa i typ):
  - `(?<itemName>[\\D]+)` - Nazwa przedmiotu, który chcesz wyrzucić i umieścić na bierzącej lokacji
  - `(?<amount>[\\d]+)` - [Opcjonalny, wartość domyślna = 1] Liczba przedmiotów, które chcesz wyrzucić i umieścić na bierzącej lokacji
- `AdminTeleportCommand` - parametry (nazwa i typ):
  - `(?<playerName>[\\w-]{4,})` - Nazwa gracza, którego chcesz teleportować
  - `(?<locationId>[\\w-]+)` - [Opcjonalny, wartość domyślna = obecna lokacja administratora] ID lokacji na jaką gracz ma być teleportowany
- `AdminGiveItemCommand` - parametry (nazwa i typ):
  - `(?<itemName>[\\D]+)` - Nazwa przedmiotu
  - `(?<playerName>[\\w-]{4,})` - [Opcjonalny, wartość domyślna = nick administratora] Nazwa gracza, który ma otrzymać przedmiot
  - `(?<amount>[\\d]+)` - [Opcjonalny, wartość domyślna = 1] Liczba przedmiotów, którą ma otrzymać gracz
- `AdminPutItemCommand` - parametry (nazwa i typ):
  - `(?<itemName>[\\D]+)` - Nazwa przedmiotu, który chcesz umieścić na bierzącej lokacji
  - `(?<amount>[\\d]+)` - [Opcjonalny, wartość domyślna = 1] Liczba przedmiotów, które chcesz umieścić na bierzącej lokacji

## Gracz
### Statystyki/karta gracza
### Parametry
### Ekwipunek
### Walka

## Skrypty
### pliki konfiguracyjne skryptów i mapowanie skryptów
Plikiem konfiguracyjnym skryptów jest plik JSON znajdujący się w folderze domyślnym [`scripts`](assets/scripts/) [[*]](#lokalizacje-plikow-gry):
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
- `player` jest typu org.grizz.game.model. **Player** - pozwala na dostęp do wszystkich statystyk gracza, jego ekwipunku, parametrów, obecnej lokalizacji,
- `response` jest typu org.grizz.game.model. **PlayerResponse** - udostępnia obiekt odpowiedzi
- `scriptId` jest typu java.lang. **String** - id uruchomionego skryptu
- `log` jest typu org.slf4j. **Logger** - logger systemowy uruchomiony w kontekście klasy ScriptRunner

W przypadku skryptów odpalanych przez gracza za pomocą komendy z parametrami, parametry te są zmapowane do zmiennych w skrypcie.
Przykładowo komenda uruchamiająca skrypt craftingu w kowadle zdefiniowana jako:
```javascript
{
    "command": "wykuj (?<itemName>[\\D]+) (?<amount>[\\d]+)",
    "scriptId": "bs-anvil-crafting"
}
```
ma zadeklarowane 2 parametry wejściowe "itemName" i "amount", które zostaną zmapowane do zmiennych w kodzie JS. Przy czym każda zmienna będzie miała typ String, a w przypadku zmiennej "amount" należy sprawdzić jej istnienie i ewentualnie zainicjalizować ją wartością domyślną (patrz linijka 17 w [`anvil/crafting.js`](assets/scripts/js/items/statics/crafting/blacksmith/anvil/crafting.js))

### Dostęp do serwisów i klas pomocniczych silnika
Zmienne pod którymi dostępne są serwisy ułatwiające interakcję ze światem gry:
Repozytoria: //TODO skonczylem tutaj
- `locationRepo` jest typu org.grizz.game.model.repository. **LocationRepo** - pozwala na pobieranie lokalizacji na podstawie ID
- `itemRepo` jest typu org.grizz.game.model.repository. **ItemRepo** - pozwala na pobieranie przedmiotów na podstawie ID lub nazwy
- `scriptRepo` jest typu org.grizz.game.model.repository. **ScriptRepo** - pozwala na pobieranie skryptów za pomocą ID
- `playerRepository` jest typu org.grizz.game.model.repository. **PlayerRepository** - pozwala na pobieranie graczy na podstawie nicku
- `locationItemsRepository` jest typu org.grizz.game.model.repository. **PlayerRepository** - pozwala na pobieranie graczy na podstawie nicku

Serwisy
adminRightsService
equipmentService
eventService
locationService
multiplayerNotificationService
commandHandler
scriptRunner

Konwertery
DBItemPackToItemListConverter
equipmentReadConverter
equipmentWriteConverter
itemListToItemStackConverter
itemReadConverter
itemStackWriteConverter
itemWriteConverter
locationItemsReadConverter
locationItemsWriteConverter

### Zwracanie wartości
Wartość jaką zwróci skrypt jest wartością zwróconą przez ostatnią funkcję w skrypcie. Przykładową konstrukcją zwracającą zawsze wartość true jest:
```javascript
function alwaysTrue() {
    // here goes all the script instructions
    return true;
}
alwaysTrue();
```
Obecnie zwracana wartość jest brana pod uwagę jedynie w przypadku skryptów beforeX na lokacjach. Skrypty są predykatami do wykonywanej lokacji i muszą zwracać wartość typu Boolean.

### Skrypty specjalne
#### Master script
#### Skrypty pomocnicze
#### Skrypt administratorski

## Przedmioty
### Model przedmiotów
Pliki JSON opisujące właściwości przedmiotów znajdują się w folderze domyślnym [`items`](assets/items/) [[*]](#lokalizacje-plikow-gry). Przykładowy wpis w pliku:
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

## Administrator
### Nadawanie praw administratora
### Komendy administratora
