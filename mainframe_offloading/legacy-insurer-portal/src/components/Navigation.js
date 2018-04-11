import React, { Component } from 'react'
import {
    Link
} from 'react-router-dom'

export default class Navigation extends Component {

    render() {
        return (
            <div className="containerBorder">
                <nav>
                <Link to="/policies?type=motor">Car Insurance Policies Overview</Link>
                    <Link to="/create-car-claim">Create Car Insurance Claim</Link>
                    <Link to="/create-car-policy">Create Car Insurance Policy</Link>
                    <Link to="/exit">Exit</Link>
                </nav>
            </div>
        )
    }
}