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
            Header: 'Car Model',
            accessor: 'car_model',
            className: "policies-table-td"
          }, {
            Header: 'Last Annual Premium Gross',
            id: 'last_ann_premium_gross',
            accessor: d => Math.round(d.last_ann_premium_gross),
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
                    defaultPageSize={100}
                />
            </div>
        )
    }
}

export default CarPoliciesList