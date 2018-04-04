export const ADD_CAR_POLICIES = 'ADD_CAR_POLICIES'
export const ADD_HOME_POLICIES = 'ADD_HOME_POLICIES'
export const ADD_CUSTOMERS = 'ADD_CUSTOMERS'

export function addCarPolicies(policies) {
    return {
        type: ADD_CAR_POLICIES,
        policies
    }
}

export function addHomePolicies(policies) {
    return {
        type: ADD_HOME_POLICIES,
        policies
    }
}

export function addCustomers(customers) {
    return {
        type: ADD_CUSTOMERS,
        customers
    }
}
