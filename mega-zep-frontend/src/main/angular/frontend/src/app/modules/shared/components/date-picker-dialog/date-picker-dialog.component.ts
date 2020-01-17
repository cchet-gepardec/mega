import {Component, Inject, OnInit} from '@angular/core';
import {Employee} from "../../models/Employee/Employee";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {UtilService} from "../../util/util.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-date-picker-dialog',
  templateUrl: './date-picker-dialog.component.html',
  styleUrls: ['./date-picker-dialog.component.scss']
})
export class DatePickerDialogComponent  implements OnInit {

  employee: Employee = null;
  date: string;

  constructor(
    @Inject(MAT_DIALOG_DATA) private data,
    private utilService: UtilService,
    private _snackBar: MatSnackBar
  ) {
  }

  ngOnInit() {
    this.employee = this.data;
  }

  updateEmployee(employee: Employee) {
    let employees: Array<Employee> = new Array<Employee>(employee);
    this.updateEmployees(employees);
  }

  updateEmployees(employees: Array<Employee>) {
    employees.forEach(employee => {
      employee.releaseDate = this.date;
    });

    this.utilService.updateEmployees(employees).subscribe((response: Response) => {
      this.openSnackBar(response['responseHeader']['message']);
    });
  }

  dateChanged(date: string) {
    this.date = date;
  }

  openSnackBar(message: string) {
    return this._snackBar.open(message, null, {
      duration: 3000,
    });
  }
}
