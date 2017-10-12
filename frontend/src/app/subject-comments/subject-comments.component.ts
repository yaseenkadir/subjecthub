import { Component, OnInit, Input } from '@angular/core';
import { SubjectCommentsService } from "../services/subject-comments.service";
import { SubjectComment } from "../models/subject-comment";
import { Utils } from '../utils/utils';

@Component({
  selector: 'app-subject-comments',
  templateUrl: './subject-comments.component.html',
  styleUrls: ['./subject-comments.component.css'],
  providers: [SubjectCommentsService]
})

export class SubjectCommentsComponent implements OnInit {
  @Input()
  universityId: number;

  @Input()
  subjectId: number;

  comments: SubjectComment[];
  fatalMessage: string = null;
  message: string = null;
  canAddComment: boolean = false;

  constructor(private subjectCommentsService: SubjectCommentsService) {
    this.canAddComment = false;
  }

  ngOnInit() {
    this.fetch();
  }

  cleanMessages() {
    this.fatalMessage = null;
    this.message = null;
  }

  fetch() {
    this.cleanMessages();
    this.subjectCommentsService.fetch(this.universityId, this.subjectId)
      .then(result => {
        this.comments = result;
        this.canAddComment = true;
      })
      .catch(e => {
        console.log(e);
        this.fatalMessage = Utils.getApiErrorMessage(e);
        this.canAddComment = false;
      });
  }

  thumbUp(commentId: number) {
    this.cleanMessages();
    this.subjectCommentsService.addThumbUp(this.universityId, this.subjectId, commentId)
      .then(comment => {
        this.handleCommentActionSuccess(comment);
      })
      .catch(e => {
        this.handleCommentActionFailed(e);
      });
  }

  thumbDown(commentId: number) {
    this.cleanMessages();
    this.subjectCommentsService.addThumbDown(this.universityId, this.subjectId, commentId)
      .then(comment => {
        this.handleCommentActionSuccess(comment);
      })
      .catch(e => {
        this.handleCommentActionFailed(e);
      });
  }

  flag(commentId: number) {
    this.cleanMessages();
    this.subjectCommentsService.flag(this.universityId, this.subjectId, commentId)
      .then(comment => {
        this.handleCommentActionSuccess(comment);
      })
      .catch(e => {
        this.handleCommentActionFailed(e);
      });
  }

  unflag(commentId: number) {
    this.cleanMessages();
    this.subjectCommentsService.unFlag(this.universityId, this.subjectId, commentId)
      .then(comment => {
        this.handleCommentActionSuccess(comment);
      })
      .catch(e => {
        this.handleCommentActionFailed(e);
      });
  }

  addComment(message: string) {
    this.cleanMessages();
    //need auth? or is spring enough
    if (this.canAddComment) {
      this.subjectCommentsService.add(this.universityId, this.subjectId, message)
        .then(comment => {
          this.handleCommentActionSuccess(comment);
        })
        .catch(e => {
          this.handleCommentActionFailed(e);
        });
    }
  }

  handleCommentActionSuccess(comment: SubjectComment) {
    this.canAddComment = true;
    this.placeComment(comment);
  }

  handleCommentActionFailed(error: any) {
    console.log(error);
    this.canAddComment = false;
    this.message = Utils.getApiErrorMessage(error);
  }

  placeComment(response: SubjectComment) {
    let found: boolean = false;
    this.comments.forEach(currentValue => {
      if (currentValue.id = response.id) {
        currentValue = response;
        found = true;
      }
    });
    if (!found) {
      this.comments.push(response);
    }
  }

}
