import React, { Component } from 'react'
import { Grid, Container } from 'semantic-ui-react'
import { createPolicy } from '../APIUtil'
import { Redirect, Link } from 'react-router-dom'
import { currentDate } from '../utils'
import { connect } from 'react-redux'
import { addCarPolicy } from '../actions'

class NewCarInsurancePolicy extends Component {

    constructor(props) {
        super(props);
        this.state = {
            policy: {
                customer_id: '',
                cover_start: currentDate(),
                car_model: '',
                max_coverd: '',
                last_ann_premium_gross: ''
            },
            policy_created: false,
            validation_errors: []
        }
    }

    handleSubmit = (event) => {
        if (this.validate() === 0) {
            console.log("Calling API with ", this.state.policy)
            createPolicy(this.state.policy, 'motor')
                .then(value => {
                    this.props.dispatch(addCarPolicy(value))
                    this.setState({
                        policy: value,
                        policy_created: true
                    })
                })
        }
        event.preventDefault();
    }

    validate = () => {
        let validation_errors = []
        if (/C\d{9,9}/.test(this.state.policy.customer_id) === false) {
            validation_errors.push("Customer ID does not match regular expression 'C\\d{9,9}'")
        }
        if (/\d{4}-\d{2}-\d{2}/.test(this.state.policy.cover_start) === false) {
            validation_errors.push("Settled Date does not match regular expression '\\d{4}-\\d{2}-\\d{2}'")
        }
        if (/\w+/.test(this.state.policy.car_model) === false) {
            validation_errors.push("Car Model does not match regular expression '\\w+'")
        }
        if (/\d+/.test(this.state.policy.max_coverd) === false) {
            validation_errors.push("Policy Maximum does not match regular expression '\\d+'")
        }
        if (/\d+/.test(this.state.policy.last_ann_premium_gross) === false) {
            validation_errors.push("Gross Premium does not match regular expression '\\d+'")
        }
        this.setState({ validation_errors })
        return validation_errors.length
    }

    handleChange = (event) => {
        console.log(event.target.id)
        let newPolicy = this.state.policy
        newPolicy[event.target.id] = event.target.value
        this.setState({ policy: newPolicy })
        this.validate()
    }

    render() {
        if (this.state.policy_created === true) {
            return <Redirect to={'/policies/'.concat(this.state.policy.policy_id, "?success")} />
        }

        // policy_id and last_change will be created by server
        return (
            <div>
                <Link to='/' className="menu-link">Back to menu</Link>
                <Container className="policyForm">
                    <ul>
                        {this.state.validation_errors.map(e =>
                            <li className="validationError">{e}</li>
                        )
                        }
                    </ul>
                    <Grid container columns={2}>
                        <Grid.Row>
                            <Grid.Column><label htmlFor="customer_id">Customer ID</label></Grid.Column>
                            <Grid.Column><input type="text" id="customer_id" value={this.state.policy.customer_id} onChange={this.handleChange} /></Grid.Column>
                        </Grid.Row>
                        <Grid.Row>
                            <Grid.Column><label htmlFor="cover_start">Cover Start (YYYY-MM-DD)</label></Grid.Column>
                            <Grid.Column><input type="text" id="cover_start" value={this.state.policy.cover_start} onChange={this.handleChange} /></Grid.Column>
                        </Grid.Row>
                        <Grid.Row>
                            <Grid.Column><label htmlFor="customer_id">Car Model</label></Grid.Column>
                            <Grid.Column><input type="text" id="car_model" value={this.state.policy.car_model} onChange={this.handleChange} /></Grid.Column>
                        </Grid.Row>
                        <Grid.Row>
                            <Grid.Column><label htmlFor="max_coverd">Policy Maximum (rounded to the full EURO)</label></Grid.Column>
                            <Grid.Column> <input type="text" id="max_coverd" value={this.state.policy.max_coverd} onChange={this.handleChange} /></Grid.Column>
                        </Grid.Row>
                        <Grid.Row>
                            <Grid.Column> <label htmlFor="last_ann_premium_gross">Gross Premium (rounded to the full EURO)</label></Grid.Column>
                            <Grid.Column><input type="text" id="last_ann_premium_gross" value={this.state.policy.last_ann_premium_gross} onChange={this.handleChange} /></Grid.Column>
                        </Grid.Row>
                        <Grid.Row>
                            <Grid.Column><input type="button" onClick={this.handleSubmit} className="formSubmitButton" value="Create Policy" /></Grid.Column>
                        </Grid.Row>
                    </Grid>
                </Container>
            </div>
        )
    }

}

export default connect()(NewCarInsurancePolicy)