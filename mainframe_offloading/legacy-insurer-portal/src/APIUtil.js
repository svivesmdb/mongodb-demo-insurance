import sleep from 'sleep-promise'

const api = "http://localhost:8080"
//const api = "http://35.198.182.83:30101"

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
    sleep(3000)(fetch(`${api}/customers`, { headers })
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

export const createClaim = (policy_id, claim, type) => {
    console.log('policy_id', policy_id)
    // Server expects form data, converting JSON to form data
    const formData = Object.keys(claim).map((key) => {
        return encodeURIComponent(key) + '=' + encodeURIComponent(claim[key]);
    }).join('&');


    console.log("createClaim payload ", formData)

    return fetch(`${api}/policies/${policy_id}?type=${type}`, {
        method: 'POST',
        headers: headersForJSONPayload,
        body: formData
    }).then(res => res.json())
}