import React, { Component } from 'react'
import { connect } from 'react-redux'
import { Link } from 'react-router-dom'
import qs from 'qs';
import { fetchPolicy } from '../APIUtil'
import { addCarPolicy } from '../actions';

class PolicyDetail extends Component {

    state = {
        success: false
    }

    componentDidMount() {
        console.log("componentDidMount PolicyDetail")
        const parsed = qs.parse(this.props.location.search.slice(1));
        const success = ("success" in parsed) ? true : false
        if (success) console.log("redirect from create policy screen")
        this.setState({ success: success })

        if (this.props.policy === undefined) {
            console.log("policy does not exist in store, fetching from server")
            fetchPolicy(this.props.match.params.id).then(value =>
                this.props.dispatch(addCarPolicy(value)))
        }
    }

    renderCarPolicy = carPolicy => (
        <div className="tile contact policy-tile" key={carPolicy.policy_id}>
            <div className="row">Customer ID: {carPolicy.customer_id}</div>
            <div className="row">Policy ID: {carPolicy.policy_id}</div>
            <div className="row">Cover Start: {carPolicy.cover_start}</div>
            <div className="row">Car Model: {carPolicy.car_model}</div>
            <div className="row">Policy Maximum: {carPolicy.max_coverd}</div>
            <div className="row">Gross Premium: {carPolicy.last_ann_premium_gross}</div>
        </div>)

    render() {

        let created = this.state.success ? <div>Policy created</div> : <div></div>

        if (this.props.policy === undefined)
            return (<div>There was an error retrieving the policy</div>)

        return (
            <div>
                <h2 className="applicationHeader"> {created} </h2>
                <div className="policyForm">
                    {this.renderCarPolicy(this.props.policy)}
                </div>
                <h2 className="applicationHeader"><Link to={"/create-car-claim/".concat(this.props.policy.policy_id)}>Create claim</Link></h2>
            </div>
        )
    }

}

const mapStateToProps = (state, ownProps) => {
    console.log("mapStateToProps PolicyDetail")
    return {
        policy: state.carPolicies.find(e => e.policy_id === ownProps.match.params.id)
    }
}

export default connect(mapStateToProps)(PolicyDetail)