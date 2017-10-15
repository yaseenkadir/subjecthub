import { Component, Input, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from '../models/subject';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Consts } from '../config/consts';
import { Utils } from '../utils/utils';

@Component({
  selector: 'app-create-subject',
  templateUrl: './create-subject.component.html',
  styleUrls: ['./create-subject.component.css']
})
export class CreateSubjectComponent implements OnInit {

  ngOnInit() {

  }
}
