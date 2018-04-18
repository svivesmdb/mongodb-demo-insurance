const api = "http://localhost:8081"

const headers = {
    'Accept': 'application/json'
}

const headersForJSONPayload = {
    'Accept': 'application/json',
    'Content-Type': 'application/x-www-form-urlencoded'
}

export const fetchPolicies = (type) =>
    fetch(`${api}/policies?type=${type}`, { headers })
        .then(res => res.json())

export const fetchCustomers = () =>
    fetch(`${api}/v2/customer`, { headers })
        .then(res => res.json())

export const fetchCustomer = (customer_id) =>
    fetch(`${api}/v2/customer/${customer_id}`, { headers })
        .then(res => res.json())

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