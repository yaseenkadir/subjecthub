import { Component, OnInit, Input } from '@angular/core';
import { SubjectCommentsService } from "../services/subject-comments.service";
import { SubjectComment } from "../models/subject-comment";
import { Utils } from '../utils/utils';
import { AuthService } from '../services/auth.service';
import { ToastrService } from 'ngx-toastr';
import { FormBuilder, FormGroup } from '@angular/forms';
import { User } from '../models/user';

@Component({
  selector: 'app-subject-comments',
  templateUrl: './subject-comments.component.html',
  styleUrls: ['./subject-comments.component.css'],
  providers: [SubjectCommentsService, AuthService, ToastrService],
})

export class SubjectCommentsComponent implements OnInit {
  @Input()
  universityId: number;

  @Input()
  subjectId: number;

  comments: SubjectComment[];
  user: User;

  addCommentForm: FormGroup;

  constructor(private subjectCommentsService: SubjectCommentsService, private authService: AuthService,
              private toastr: ToastrService, private fb: FormBuilder) {
    this.addCommentForm = fb.group({
      "comment": [
        null
        ]
    });

    this.comments = [];

    if (authService.isLoggedIn()) {
      this.user = this.authService.currentUser() || null;
    }
  }

  ngOnInit() {
    this.fetch();
  }

  fetch() {
    this.subjectCommentsService.fetch(this.universityId, this.subjectId)
      .then(result => {
        this.comments = result;
      })
      .catch(e => {
        console.log(e);
      });
  }

  thumbUp(commentId: number) {
    if(this.checkLoggedIn()){
      this.subjectCommentsService.addThumbUp(this.universityId, this.subjectId, commentId)
        .then(comment => {
          console.log("thumb up added to comment");
          this.refresh();
        })
        .catch(e => {
          this.handleCommentActionFailed(e);
        });
    }
  }

  thumbDown(commentId: number) {
    if(this.checkLoggedIn()){
      this.subjectCommentsService.addThumbDown(this.universityId, this.subjectId, commentId)
        .then(comment => {
          console.log("thumbs down added to comment");
          this.refresh();
        })
        .catch(e => {
          this.handleCommentActionFailed(e);
        });
    }
  }

  flag(commentId: number) {
    if(this.checkLoggedIn()){
      this.subjectCommentsService.flag(this.universityId, this.subjectId, commentId)
        .then(comment => {
          console.log("flagged comment");
          this.refresh();
        })
        .catch(e => {
          this.handleCommentActionFailed(e);
        });
    }
  }

  unflag(commentId: number) {
    if(this.checkLoggedIn()){
      this.subjectCommentsService.unFlag(this.universityId, this.subjectId, commentId)
        .then(comment => {
          console.log("unflagged comment");
          this.refresh();
        })
        .catch(e => {
          this.handleCommentActionFailed(e);
        });
    }
  }

  addComment(comment: object) {
    if (this.checkLoggedIn()) {
      this.subjectCommentsService.add(this.universityId, this.subjectId, comment)
        .then(comment => {
          console.log("added comment");
          this.refresh();
        })
        .catch(e => {
          this.handleCommentActionFailed(e);
        });
    }
  }

  handleCommentActionFailed(error: any) {
    console.log(error);
  }

  checkLoggedIn(): boolean {
    if (!this.authService.isLoggedIn()){
      this.toastr.error("Not logged in!");
      return false;
    }else{
      return true;
    }
  }

  refresh(){
    location.reload();
  }
}
