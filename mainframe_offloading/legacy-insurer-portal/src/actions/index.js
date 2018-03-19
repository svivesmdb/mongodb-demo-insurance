export const ADD_CAR_POLICIES = 'ADD_CAR_POLICIES'

export function addCarPolicies(policies) {
    return {
      type: ADD_CAR_POLICIES,
      policies
    }
  }