import { ADD_CAR_POLICIES, ADD_CUSTOMERS } from '../actions'

const initialState = {
    carPolicies: [],
    customers: []
}

function reducer(state = initialState, action) {
    console.log("reducer executed", action)
    switch (action.type) {
        case ADD_CAR_POLICIES:
            return {
                ...state,
                "carPolicies": action.policies
            }
        case ADD_CUSTOMERS:
            return {
                ...state,
                "customers": action.customers
            }
        default:
            return state
    }
}

export default reducer;