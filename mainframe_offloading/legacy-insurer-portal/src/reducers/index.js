import { ADD_CAR_POLICIES } from '../actions'

const initialState = {
    carPolicies: []
}

function reducer(state = initialState, action) {
    console.log("reducer executed", action)
    switch(action.type) {
        case ADD_CAR_POLICIES:
            return {
                "carPolicies": action.policies
            }
        default:
            return state
    }
}

export default reducer;