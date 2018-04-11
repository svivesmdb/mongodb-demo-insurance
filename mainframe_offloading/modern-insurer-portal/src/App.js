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
import CustomerDetail from './components/CustomerDetail';
import { fetchCustomers } from './APIUtil';
import { addCustomers } from './actions';
import { connect } from 'react-redux'
import CustomerList from './components/CustomerList';

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
        <Router>
          <div className="container">
          <div>
            <nav>
              <ul className="nav-ul">
                <li>
                  <Link to="/customers">Customers</Link>
                </li>
                <li>
                  <Link to="/policies">Policies</Link>
                </li>
                <li>
                  <Link to="/create">Create Policy</Link>
                </li>
              </ul>
            </nav>
            </div>
            <div>
              <h1>Insurance 360°</h1>
            </div>
            <div>
              <Route exact path="/customers" component={CustomerList} />
              <Route exact path="/customers/:id" component={CustomerDetail} />
              <Route exact path="/create" component={NewCarInsurancePolicy} />
              <Route exact path="/policies" component={PoliciesList} />
            </div>
          </div>
        </Router>
      </div>
    );
  }
}

export default connect()(App);
