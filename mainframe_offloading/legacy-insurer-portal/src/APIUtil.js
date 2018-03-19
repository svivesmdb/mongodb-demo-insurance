const api = "http://localhost:8080"

const headers = {
    'Accept': 'application/json'
  }

export const fetchCarPolicies = () =>
    fetch(`${api}/policies?type=motor`, { headers })
    .then(res => res.json())