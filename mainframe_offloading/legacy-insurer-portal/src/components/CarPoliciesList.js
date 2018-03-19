import React, { Component } from 'react'
import ReactTable from 'react-table'
import 'react-table/react-table.css'

class CarPoliciesList extends Component {
    render() {
        const columns = [{
            Header: 'Policy ID',
            accessor: 'policy_id',
            className: "policies-table-td"
          }, {
            Header: 'Customer ID',
            accessor: 'customer_id',
            className: "policies-table-td"
          }, {
            Header: 'Home Type',
            accessor: 'home_type',
            className: "policies-table-td"
          }, {
            Header: 'Last Annual Premium Gross',
            accessor: 'last_ann_premium_gross',
            className: "policies-table-td"
          }, {
            Header: 'Max Coverage',
            accessor: 'max_coverd',
            className: "policies-table-td"
          }, {
            Header: 'Last Change',
            accessor: 'last_change',
            className: "policies-table-td"
          }]

        return (
            <div className="policies-table">
                <ReactTable
                    data={this.props.carPolicies}
                    columns={columns}
                />
            </div>
        )
    }
}

export default CarPoliciesList