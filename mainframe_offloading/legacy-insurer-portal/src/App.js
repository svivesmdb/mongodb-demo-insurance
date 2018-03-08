import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import NewCarInsurancePolicy from './components/NewCarInsurancePolicy';
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
            <Route path="/navigation" component={Navigation} />
            <Route path="/createcarpolicy" component={NewCarInsurancePolicy} />
          </div>
        </Router>
      </div>
    );
  }
}

export default App;
