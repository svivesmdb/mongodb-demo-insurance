import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import PoliciesList from './components/PoliciesList';
import Navigation from './components/Navigation';
import {
  BrowserRouter as Router,
  Route
} from 'react-router-dom'
import PolicyDetail from './components/PolicyDetail';
import { fetchCustomers } from './APIUtil';
import { addCustomers } from './actions';
import { connect } from 'react-redux'
import CustomerList from './components/CustomerList';
import CreateCarInsurancePolicy from './components/CreateCarInsurancePolicy';
import CreateCarInsuranceClaim from './components/CreateCarInsuranceClaim';

class App extends Component {

  componentDidMount() {
    fetchCustomers().then((data) => {
      console.log(data);
      this.props.dispatch(addCustomers(data))
    });
  }

  render() {
    return (
      <div className="app">
        <div className="applicationHeader">
          <h1>Insurer Portal v1.0</h1>
        </div>
        <Router>
          <div>
            <Route exact path="/" component={Navigation} />
            <Route exact path="/customers" component={CustomerList} />
            <Route exact path="/create-car-policy" component={CreateCarInsurancePolicy} />
            <Route exact path="/policies" component={PoliciesList} />
            <Route exact path="/policies/:id" component={PolicyDetail} />
            <Route path="/create-car-claim" component={CreateCarInsuranceClaim} />
          </div>
        </Router>
      </div>
    );
  }
}

export default connect()(App);
