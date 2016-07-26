//@ sourceURL=mud-core/src/main/resources/scripts/js/master-script.js
//line above is for IntelliJ debugging purposes

function loadScript(id) {
  var _script = scriptRepo.get(id);
  var FileUtils = Java.type("old.org.grizz.game.utils.FileUtils");
  logger.info("Loading inner script [{}] described as [{}]", [_script.getPath(), _script.getName()]);

  var _scriptRelativePath = _script.getPath();
  var _scriptJavaPath = FileUtils.getFilepath(_scriptRelativePath);
  var _scriptFilePath = _scriptJavaPath.toString();

  return load(_scriptFilePath);
}

loadScript("game-utils");
loadScript(scriptId);