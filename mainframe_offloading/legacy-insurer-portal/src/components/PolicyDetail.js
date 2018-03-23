import React, { Component } from 'react'
import { connect } from 'react-redux'

class PolicyDetail extends Component {

    render(){
        return (
            <ul>
                <li>Policy ID:</li>
            </ul>
         )
    }

}

const mapStateToProps = (state, ownProps) => {
    console.log("state.carPolicies.length", state.carPolicies.length)
    return {
        policy: state.carPolicies.find(e =>  e.policy_id === ownProps.match.params.id)
    }
}

export default connect(mapStateToProps)(PolicyDetail)