import React, { Component } from 'react'
import { Grid, Container } from 'semantic-ui-react'
import { createClaim } from '../APIUtil'
import { Redirect, Link } from 'react-router-dom'
import { currentDate } from '../utils'
import { addCarClaim } from '../actions'
import { connect } from 'react-redux'

class CreateCarInsuranceClaim extends Component {

    constructor(props) {
        super(props);
        this.state = {
            claim: {
                policy_id: this.props.match.params.id,
                claim_date: currentDate(),
                claim_amount: '',
                settled_date: '',
                settled_amount: '',
                claim_reason: ''
            },
            claim_created: false,
            validation_errors: []
        }
    }

    handleSubmit = (event) => {
        if (this.validate() === 0) {
            console.log("Calling API with ", this.state.claim)

            createClaim(this.state.claim.policy_id, this.state.claim, 'motor').then(value => {
                this.setState({
                    claim: value,
                    claim_created: true
                })
                this.props.dispatch(addCarClaim(value))
            })
        }
        event.preventDefault();
    }

    validate = () => {
        let validation_errors = []
        if (/PC_\d+/.test(this.state.claim.policy_id) === false) {
            validation_errors.push("Policy ID does not match regular expression 'PC_\\d+'")
        }
        if (/\d{4}-\d{2}-\d{2}/.test(this.state.claim.claim_date) === false) {
            validation_errors.push("Claim Date does not match regular expression '\\d{4}-\\d{2}-\\d{2}'")
        }
        if (/\d+/.test(this.state.claim.claim_amount) === false) {
            validation_errors.push("Claim Amount does not match regular expression '\\d+'")
        }
        if (/\d{4}-\d{2}-\d{2}/.test(this.state.claim.settled_date) === false) {
            validation_errors.push("Settled Date does not match regular expression '\\d{4}-\\d{2}-\\d{2}'")
        }
        if (/\d+/.test(this.state.claim.settled_amount) === false) {
            validation_errors.push("Settled Amount does not match regular expression '\\d+'")
        }
        if (/\w+/.test(this.state.claim.claim_reason) === false) {
            validation_errors.push("Claim Reason does not match regular expression '\\w+'")
        }
        this.setState({ validation_errors })
        return validation_errors.length
    }


    handleChange = (event) => {
        console.log(event.target.id)
        let claim = this.state.claim
        claim[event.target.id] = event.target.value
        this.setState({ claim: claim })
        this.validate()
    }

    render() {
        if (this.state.claim_created === true) {
            return <Redirect to={'/claims/'.concat(this.state.claim.claim_id)} />
        }

        // policy_id and last_change will be created by server
        return (
            <div>
                <Link to='/' className="menu-link">Back to menu</Link>
                <Container className="policyForm">
                    <ul>
                        {this.state.validation_errors.map(e =>
                            <li className="validationError" key={e}>{e}</li>
                        )
                        }
                    </ul>
                    <Grid container columns={2}>
                        <Grid.Row>
                            <Grid.Column><label htmlFor="policy_id">Policy ID</label></Grid.Column>
                            <Grid.Column><input type="text" id="policy_id" value={this.state.claim.policy_id} onChange={this.handleChange} /></Grid.Column>
                        </Grid.Row>
                        <Grid.Row>
                            <Grid.Column><label htmlFor="claim_date">Claim Date (YYYY-MM-DD)</label></Grid.Column>
                            <Grid.Column><input type="text" id="claim_date" value={this.state.claim.claim_date} onChange={this.handleChange} /></Grid.Column>
                        </Grid.Row>
                        <Grid.Row>
                            <Grid.Column><label htmlFor="claim_amount">Claim Amount (rounded to the full EURO)</label></Grid.Column>
                            <Grid.Column><input type="text" id="claim_amount" value={this.state.claim.claim_amount} onChange={this.handleChange} /></Grid.Column>
                        </Grid.Row>
                        <Grid.Row>
                            <Grid.Column><label htmlFor="settled_date">Settled Date (YYYY-MM-DD)</label></Grid.Column>
                            <Grid.Column> <input type="text" id="settled_date" value={this.state.claim.settled_date} onChange={this.handleChange} /></Grid.Column>
                        </Grid.Row>
                        <Grid.Row>
                            <Grid.Column><label htmlFor="settled_amount">Settled Amount (rounded to the full EURO)</label></Grid.Column>
                            <Grid.Column> <input type="text" id="settled_amount" value={this.state.claim.settled_amount} onChange={this.handleChange} /></Grid.Column>
                        </Grid.Row>
                        <Grid.Row>
                            <Grid.Column><label htmlFor="settled_amount">Claim Reason</label></Grid.Column>
                            <Grid.Column> <input type="text" id="claim_reason" value={this.state.claim.claim_reason} onChange={this.handleChange} /></Grid.Column>
                        </Grid.Row>
                        <Grid.Row>
                            <Grid.Column><input type="button" onClick={this.handleSubmit} className="formSubmitButton" value="Create Claim" /></Grid.Column>
                        </Grid.Row>
                    </Grid>
                </Container>
            </div>
        )
    }
}

export default connect()(CreateCarInsuranceClaim)