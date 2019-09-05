import {HttpHeaders} from "@angular/common/http";

export const configuration = {
  // Base url
  BASEURL: 'http://localhost:8080',

  PAGES: {
    HOME: '/home',
    DASHBOARD: '/dashboard',
    LOGIN: '/login',
    EMPLOYEES: '/employees'
  },

  EMPLOYEE_FUNCTIONS: {
    '01': 'Technischer PL',
    '02': 'Softwareentwickler',
    '03': 'Verwaltung',
    '04': 'Senior',
    '05': 'Junior',
    '06': 'Experte Inbetriebnahme',
    '06-1': 'Software-Architekt',
    '07': 'Ferialpraktikant',
    '08': 'Consultant Senior',
    '99': 'Reisezeiten'
  },

  // Http Headers
  httpOptions: {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
    }),
    withCredentials: true
  },

  dateFormat: 'yyyy-MM-dd'
};
