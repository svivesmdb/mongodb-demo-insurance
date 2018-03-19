import React, { Component } from 'react'
import { connect } from 'react-redux'
import { fetchPolicies } from '../APIUtil'
import { addCarPolicies, addHomePolicies } from '../actions'
import queryString from 'query-string'
import CarPoliciesList from './CarPoliciesList'

class PoliciesList extends Component {

    state = {
        policyType: 'motor',
        loading: true
    }

    componentDidMount() {
        const parsed = queryString.parse(this.props.location.search);
        const policyType = ("type" in parsed) ? parsed.type : 'motor'
        console.log("queryString parsed: ", parsed, ", calculated type: ", policyType)
        this.setState({ policyType: policyType })

        fetchPolicies(policyType).then((data) => {
            console.log(data);
            if (policyType === "motor") this.props.dispatch(addCarPolicies(data))
            if (policyType === "home") this.props.dispatch(addHomePolicies(data))
        });
    }

    componentWillReceiveProps(nextProps) {
        console.log("nextProps", nextProps)
        if (nextProps.carPolicies) {
          this.setState(
            {
                ...this.state,
                loading: false
            }
          )
        }
      }

    render() {
        if(this.state.loading){
            return (
                <div className="loading"/>
            )
        }
        if (this.state.policyType === "motor")
            return (<CarPoliciesList carPolicies={this.props.carPolicies} />)


    }
}

const mapStateToProps = state => {
    return {
        carPolicies: state.carPolicies
    }
}

export default connect(mapStateToProps)(PoliciesList);
