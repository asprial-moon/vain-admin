export function validateUsername(str) {
  const username = new RegExp('[A-Za-z0-9_\-\u4e00-\u9fa5]+')
  return str !== undefined && str.length >= 3 && username.test(str)
}

export function validatePassword(password) {
  const passwordRegExp = new RegExp('^[0-9]*(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))+[a-zA-Z]*$')
  return passwordRegExp.test(password)
}
