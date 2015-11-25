//@ sourceURL=sud-core/src/main/resources/scripts/js/master-script.js
//line above is for IntelliJ debugging purposes

function loadScript(id) {
    var _script = scriptRepo.get(id);
    logger.info("Loading inner script [{}] described as [{}]", [_script.getPath(), _script.getName()]);

    var _scriptRelativePath = _script.getPath();
    var _scriptJavaPath = org.grizz.game.utils.FileUtils.getFilepath(_scriptRelativePath);
    var _scriptFilePath = _scriptJavaPath.toString();

    return load(_scriptFilePath);
}

loadScript("game-utils");
loadScript(scriptId);