import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import NewCarInsurancePolicy from './components/NewCarInsurancePolicy';
import PoliciesList from './components/PoliciesList';
import Navigation from './components/Navigation';
import {
  BrowserRouter as Router,
  Route,
  Link
} from 'react-router-dom'


class App extends Component {
  render() {
    return (
      <div className="app">
        <div className="applicationHeader">
          <h1>Insurer Portal v1.0</h1>
        </div>
        <Router>
          <div>
            <Route exact path="/" component={Navigation} />
            <Route path="/createcarpolicy" component={NewCarInsurancePolicy} />
            <Route path="/policies" component={PoliciesList} />
          </div>
        </Router>
      </div>
    );
  }
}

export default App;
