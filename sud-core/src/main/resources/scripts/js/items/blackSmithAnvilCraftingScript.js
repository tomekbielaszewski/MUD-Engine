var ironOre = "401";
var coal = "402";
var roughStone = "403";
var silverNugget = "404";
var goldNugget = "405";
var mithril = "406";

var recipes = [
    {id: "407", ingredients: [ironOre, ironOre, coal]}, //sztabka zelaza
    {id: "408", ingredients: [goldNugget, goldNugget, goldNugget, goldNugget]}, //sztabka zlota
    {id: "409", ingredients: [silverNugget, silverNugget, silverNugget, silverNugget]}, //sztabka srebra
    {id: "410", ingredients: [mithril, mithril, mithril, mithril]}  //sztabka mithrilu
];

function result() {
    //moze uzyc load('file.js') do ladowania kilku skryptow?
    return true;
}
result();