import React, { Component } from 'react'
import { connect } from 'react-redux'

class PoliciesList extends Component {

    render() {
        return (
            <div>
                <ul>
                    {
                        this.props.policies.map((policy, idx) =>
                            <li key={idx}>{policy.policy_id}</li>
                        )
                    }
                </ul>
            </div>
        )
    }
}

const mapStateToProps = state => {
    return {
        policies: state.policies
    }
}

export default connect(mapStateToProps)(PoliciesList);
