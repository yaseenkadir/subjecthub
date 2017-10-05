import { Component, OnInit } from '@angular/core';
import {SubjectService} from '../services/subject.service';
import {Subject} from '../models/subject'
@Component({
  selector: 'app-subject-details',
  templateUrl: './subject-details.component.html',
  styleUrls: ['./subject-details.component.css'],
    providers: [SubjectService]
})
export class SubjectDetailsComponent implements OnInit {

  universityId: number;
  subjectId: number;
  subject: Subject;

  constructor(private subjectService: SubjectService) {
      this.universityId = 1;
      this.subjectId = 1;
  }

  ngOnInit() {
      this.subjectService.fetch(this.universityId, this.subjectId)
          .then(subject => {
            this.subject = subject;
          })
  }

}
