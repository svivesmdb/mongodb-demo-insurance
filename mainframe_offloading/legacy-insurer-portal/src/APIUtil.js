import sleep from 'sleep-promise'

const api = "http://localhost:8080"

const headers = {
    'Accept': 'application/json'
  }


export const fetchPolicies = (type) =>
    sleep(5000)(fetch(`${api}/policies?type=${type}`, { headers })
    .then(res => res.json()))