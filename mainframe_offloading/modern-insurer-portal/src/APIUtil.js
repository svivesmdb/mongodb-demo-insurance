import sleep from 'sleep-promise'

const api = "http://localhost:8080"

const headers = {
    'Accept': 'application/json'
}

const headersForJSONPayload = {
    'Accept': 'application/json',
    'Content-Type': 'application/x-www-form-urlencoded'
}

export const fetchPolicies = (type) =>
    sleep(3000)(fetch(`${api}/policies?type=${type}`, { headers })
    .then(res => res.json()))

export const fetchCustomers = () =>
    sleep(1000)(fetch(`${api}/v2/customer`, { headers })
    .then(res => res.json()))

export const createPolicy = (policy, type) => {
    // Server expects form data, converting JSON to form data
    const formData = Object.keys(policy).map((key) => {
        return encodeURIComponent(key) + '=' + encodeURIComponent(policy[key]);
      }).join('&');


    console.log("createPolicy payload ", formData)

    return fetch(`${api}/policies?type=${type}`, {
        method: 'POST',
        headers: headersForJSONPayload,
        body: formData
    }).then(res => res.json())
}