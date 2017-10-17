import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { SubjectService } from '../services/subject.service';
import { Subject } from '../models/subject';
import { User } from '../models/user';
import { Subscription } from 'rxjs/Subscription';
import { AuthService } from '../services/auth.service';
import { Utils } from '../utils/utils';
import { ToastrService } from 'ngx-toastr';
import { Location } from '@angular/common';

@Component({
  selector: 'app-subject-details',
  templateUrl: './subject-details.component.html',
  styleUrls: ['./subject-details.component.css'],
  providers: [SubjectService, AuthService]
})
export class SubjectDetailsComponent implements OnInit {

  universityId: string;
  subjectId: string;
  subject: Subject;
  isLoading: boolean;
  user: User = null;

  constructor(private route: ActivatedRoute,
              private subjectService: SubjectService,
              private authService: AuthService,
              private location: Location,
              private toastr: ToastrService) {
  }

  fetchSubject() {
    return this.subjectService.getSubject(this.universityId, this.subjectId);
  }

  formatSubject(subject: Subject): Subject {
    const session = [];
    if (subject.undergrad) subject.studentType = 'Undergraduate';
    if (subject.postgrad) subject.studentType = 'Postgraduate';

    if (subject.summer) session.push('Summer');
    if (subject.autumn) session.push('Autumn');
    if (subject.spring) session.push('Spring');

    subject.session = session.join(', ');
    console.log(subject);
    return subject;
  }

  ngOnInit() {
    this.isLoading = true;
    this.route.paramMap
      .switchMap((params: ParamMap) => {
        this.universityId = params.get('university');
        this.subjectId = params.get('subject');
        return this.fetchSubject();
      })
      .subscribe(subject => {
        this.subject = this.formatSubject(subject);
        this.isLoading = false;
      });

    this.user = this.authService.currentUser() || null;
  }

  deleteSubject() {
    this.isLoading = true;
    let subjectName = this.subject.name;
    this.subjectService.deleteSubject(this.universityId, this.subjectId)
      .then( response => {
        this.toastr.success(`Deleted ${subjectName}`);
        this.isLoading = false;
        this.location.back();
      })
      .catch(error => {
        let errorMessage = Utils.getApiErrorMessage(error);
        this.toastr.error(errorMessage, `Unable to delete`, {timeOut: 5000})
        this.isLoading = false;
      })
  }

  navigateBack() {
    this.location.back();
  }

}
