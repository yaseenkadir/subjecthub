import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';

import {SubjectService} from '../services/subject.service';
import {Subject} from '../models/subject'
@Component({
  selector: 'app-subject-details',
  templateUrl: './subject-details.component.html',
  styleUrls: ['./subject-details.component.css'],
    providers: [SubjectService]
})
export class SubjectDetailsComponent implements OnInit {

  universityId: string;
  subjectId: string;
  subject: Subject;

  constructor(    private route: ActivatedRoute, private subjectService: SubjectService) {
      this.universityId;
      this.subjectId;
  }

  fetchSubject() {
    return this.subjectService.fetch(this.universityId, this.subjectId)
  }

  ngOnInit() {
    this.route.paramMap
    .switchMap((params: ParamMap) => {
       this.universityId = params.get('university');
       this.subjectId = params.get('subject');

       return this.fetchSubject();
    })
    .subscribe(subject =>  {
        this.subject = subject;
    });
  }

}
