import React, { Component } from 'react'
import { connect } from 'react-redux'
import { Link } from 'react-router-dom'

class PolicyDetail extends Component {

    renderCarPolicy = carPolicy => (
        <div className="tile contact policy-tile" key={carPolicy.policy_id}>
            <div className="header">Car Insurance <br></br>{carPolicy.policy_id}</div>
            <div className="row"><div className="key">Car model</div><div className="value forty-margin">{carPolicy.car_model}</div></div>
            <div className="row"><div className="key">Cover Start</div><div className="value forty-margin">{carPolicy.cover_start}</div></div>
            <div className="row"><div className="key">#Claims</div><div className="value forty-margin">{carPolicy.claim.length}</div></div>
        </div>)

    renderCarClaim = carClaim => (
        <div className="tile contact claim-tile">
            <div className="header">Claim</div>
            <div className="row"><div className="key">Reason</div><div className="value forty-margin">{carClaim.claim_reason}</div></div>
        </div>
    )

    render() {

        let items = []

        for (let j = 0; j < this.props.policy.claim.length; j++) {
            items.push(this.renderCarClaim(this.props.policy.claim[j]))
        }


        return (
            <div>
                {items}
                <Link to={"/create-car-claim/".concat(this.props.policy.policy_id)}>Create claim</Link>
            </div>
        )
    }

}

const mapStateToProps = (state, ownProps) => {
    console.log("state.carPolicies.length", state.carPolicies.length)
    return {
        policy: state.carPolicies.find(e => e.policy_id === ownProps.match.params.id)
    }
}

export default connect(mapStateToProps)(PolicyDetail)