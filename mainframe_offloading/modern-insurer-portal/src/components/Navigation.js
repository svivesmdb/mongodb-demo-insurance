import React, { Component } from 'react'
import {
    Link
} from 'react-router-dom'

export default class Navigation extends Component {

    render() {
        return (
            <div className="containerBorder">
                <nav>
                    <Link to="/customers">View Customers</Link>
                    <Link to="/createcustomer">Create Customer</Link>
                    <Link to="/createmotorpolicy">Create Motor Insurance Policy</Link>
                    <Link to="/policies?type=motor">Motor Insurance Policies Overview</Link>
                    <Link to="/exit">Exit</Link>
                </nav>
            </div>
        )
    }
}