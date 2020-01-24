import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DatePickerDialogComponent} from '../employees/components/employees-grid-list/date-picker-dialog/date-picker-dialog.component';
import {AngularMaterialModule} from '../../material-module';
import {LoginComponent} from './components/login/login.component';
import {DatepickerComponent} from './util/datepicker/datepicker.component';
import {CalenderComponent} from './util/calender/calender.component';
import {HeaderComponent} from './components/header/header.component';
import {UserActionsComponent} from './components/header/user-actions/user-actions.component';
import {RouterModule} from '@angular/router';


@NgModule({
  declarations: [
    DatePickerDialogComponent,
    DatepickerComponent,
    CalenderComponent,
    HeaderComponent,
    UserActionsComponent,
    LoginComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    AngularMaterialModule
  ],
  exports: [
    DatePickerDialogComponent,
    DatepickerComponent,
    CalenderComponent,
    HeaderComponent,
    UserActionsComponent,
    LoginComponent
  ],
  entryComponents: [
    DatePickerDialogComponent
  ]
})
export class SharedModule {
}
