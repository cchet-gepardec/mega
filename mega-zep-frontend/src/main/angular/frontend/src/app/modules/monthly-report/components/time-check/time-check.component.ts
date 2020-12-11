import { Component, Input, OnInit } from '@angular/core';
import { MonthlyReport } from '../../models/MonthlyReport';
import { State } from '../../../shared/models/State';
import {TimeWarning} from "../../models/TimeWarning";

@Component({
  selector: 'app-time-check',
  templateUrl: './time-check.component.html',
  styleUrls: ['./time-check.component.scss']
})
export class TimeCheckComponent implements OnInit {
  @Input() monthlyReport: MonthlyReport;
  displayedColumns = ['warning', 'dateTime', 'restTime', 'breakTime', 'workingTime'];
  State = State;

  constructor() {
  }

  ngOnInit(): void {
  }

  displayWarningsTooltip(timeWarning: TimeWarning){
    if(timeWarning.warnings == null || timeWarning.warnings.length == 0){
      return null;
    } else {
      return timeWarning.warnings.join("<br>");
    }
  }
}
