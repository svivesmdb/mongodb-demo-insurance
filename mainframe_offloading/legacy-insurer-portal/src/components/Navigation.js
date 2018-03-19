import React, { Component } from 'react'
import {
    BrowserRouter as Router,
    Route,
    Link
} from 'react-router-dom'

export default class Navigation extends Component {

    render() {
        return (
            <div className="containerBorder">
                <nav>
                    <Link to="/viewcustomers">View Customers</Link>
                    <Link to="/createcustomer">Create Customer</Link>
                    <Link to="/createcarpolicy">Create Car Insurance Policy</Link>
                    <Link to="/createhomepolicy">Create Home Insurance Policy</Link>
                    <Link to="/policies?type=motor">Motor Policies List</Link>
                    <Link to="/exit">Exit</Link>
                </nav>
            </div>
        )
    }
}