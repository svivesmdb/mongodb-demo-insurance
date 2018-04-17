import React, { Component } from 'react'
import { connect } from 'react-redux'
import { Link } from 'react-router-dom'

class ClaimDetail extends Component {

    state = {
        success: false
    }

    componentDidMount() {
        console.log("componentDidMount ClaimDetail")
    }

    renderCarClaim = carClaim => (
        <div className="tile contact claim-tile">
            <div className="row">Policy ID: {carClaim.policy_id}</div>
            <div className="row">Claim Date: {carClaim.claim_date}</div>
            <div className="row">Claim Amount: {carClaim.claim_amount}</div>
            <div className="row">Settled Date: {carClaim.settled_date}</div>
            <div className="row">Settled Amount: {carClaim.settled_amount}</div>
            <div className="row">Claim Reason: {carClaim.claim_reason}</div>
        </div>
    )

    render() {
        if (this.props.claim === undefined)
            return (<div></div>)

        return (
            <div>
                <Link to='/' className="menu-link">Back to menu</Link>
                <h2 className="applicationHeader"> {this.state.success && <div>Claim created</div>}  </h2>
                <div className="policyForm">
                    {this.renderCarClaim(this.props.claim)}
                </div>
            </div>
        )
    }

}

const mapStateToProps = (state, ownProps) => {
    console.log("mapStateToProps PolicyDetail")
    return {
        claim: state.carClaims.find(e => e.claim_id === ownProps.match.params.id)
    }
}

export default connect(mapStateToProps)(ClaimDetail)