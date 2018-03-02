import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import NewCarInsurancePolicy from './components/NewCarInsurancePolicy';

class App extends Component {
  render() {
    return (
      <div className="app">
      <div className="applicationHeader">
          <h1>Insurer Portal v1.0</h1>
          </div>
        <NewCarInsurancePolicy />
      </div>
    );
  }
}

export default App;
