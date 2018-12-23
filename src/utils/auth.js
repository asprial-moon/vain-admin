import Cookies from 'js-cookie'

const sessionStorage = window.sessionStorage
const TokenKey = 'Token'
const Information = 'Information'
const Environment = 'Environment'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function getInformation() {
  return JSON.parse(sessionStorage.getItem(Information))
}

export function setInformation(information) {
  return sessionStorage.setItem(Information, JSON.stringify(information))
}

export function removeInformation() {
  return sessionStorage.removeItem(Information)
}

export function setEnvironment(environment) {
  return sessionStorage.setItem(Environment, JSON.stringify(environment))
}

export function getEnvironment() {
  return JSON.parse(sessionStorage.getItem(Environment))
}

export function removeEnvironment() {
  return sessionStorage.removeItem(Environment)
}
