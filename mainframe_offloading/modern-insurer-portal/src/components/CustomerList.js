import React, { Component } from 'react'
import ReactTable from 'react-table'
import 'react-table/react-table.css'
import { Redirect } from 'react-router-dom'
import { connect } from 'react-redux'

class CustomerList extends Component {

  state = {
    customerClicked: false,
    customer_id: null
  }

  render() {
    const columns = [{
      Header: 'Customer ID',
      accessor: 'customer_id',
      className: "policies-table-td"
    }, {
      Header: 'First Name',
      accessor: 'first_name',
      className: "policies-table-td"
    }, {
      Header: 'Last Name',
      accessor: 'last_name',
      className: "policies-table-td"
    }, {
      Header: 'Gender',
      accessor: 'gender',
      className: "policies-table-td"
    }, {
      Header: 'E-Mail',
      accessor: 'email',
      className: "policies-table-td"
    }, {
      Header: 'Date of birth',
      accessor: 'date_of_birth',
      className: "policies-table-td"
    }]



      if (this.state.policyClicked === true) {
        return <Redirect push to={'/policies/'.concat(this.state.policy_id)} />
      } else return (
      <div className="policies-table">
        <ReactTable
          data={this.props.customers}
          columns={columns}
          defaultPageSize={100}
        />
      </div>
    )
  }
}

const mapStateToProps = state => {
    return {
        customers: state.customers
    }
}

export default connect(mapStateToProps)(CustomerList);