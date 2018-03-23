import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import NewCarInsurancePolicy from './components/NewCarInsurancePolicy';
import PoliciesList from './components/PoliciesList';
import Navigation from './components/Navigation';
import {
  BrowserRouter as Router,
  Route
} from 'react-router-dom'
import PolicyDetail from './components/PolicyDetail';


class App extends Component {
  render() {
    return (
      <div className="app">
        <div className="applicationHeader">
          <h1>Insurer Portal v1.0</h1>
        </div>
        <Router>
          <div>
            <Route exact path="/navigation" component={Navigation} />
            <Route exact path="/createmotorpolicy" component={NewCarInsurancePolicy} />
            <Route exact path="/policies" component={PoliciesList} />
            <Route exact path="/policies/:id" component={PolicyDetail} />
          </div>
        </Router>
      </div>
    );
  }
}

export default App;
