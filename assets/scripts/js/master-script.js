//@ sourceURL=assets/scripts/js/master-script.js
//line above is for IntelliJ debugging purposes

function loadScript(id) {
  var _script = scriptRepo.get(id);
  logger.info("Executing script [{}] described as [{}] with id [{}]", [_script.getPath(), _script.getName(), id]);

  var _scriptRelativePath = _script.getPath();
  var _scriptJavaPath = fileUtils.getFilepath(_scriptRelativePath);
  var _scriptFilePath = _scriptJavaPath.toString();

  return load(_scriptFilePath);
}

loadScript("game-sdk");
loadScript(scriptId);