import { Component, OnInit } from '@angular/core';
import {SubjectCommentsService} from "../services/subject-comments.service";
import {SubjectComment} from "../models/subject-comment";

@Component({
  selector: 'app-subject-comments',
  templateUrl: './subject-comments.component.html',
  styleUrls: ['./subject-comments.component.css'],
  providers: [SubjectCommentsService]
})

export class SubjectCommentsComponent implements OnInit {

  comments: SubjectComment[];
  fatalMessage: string = null;
  message: string = null;
  canAddComment: boolean = false;
  universityId: number = 1; //to be provided by parent in the future
  subjectId: number = 1; //to be provided by parent in the future

  constructor(private subjectCommentsService: SubjectCommentsService) {
      this.fetch();
  }

  ngOnInit() {  }

  fetch(){
      this.fatalMessage = null;
      this.canAddComment = false;
      this.subjectCommentsService.fetch(this.universityId, this.subjectId)
          .then(result => {
              if(result.length > 0){
                  this.comments = result;
              }else{
                  this.fatalMessage = "No comments found.";
              }
              if(this.isAuth()) {
                  this.canAddComment = true;
              }
          })
          .catch(e => {
              this.fatalMessage = "Comments system has encountered an error, please refresh.";
              this.canAddComment = false;
          });
  }

  isAuth(){
      //stub for auth check, not sure how
      this.canAddComment = true;
  }

  thumbUp(commentId){
      this.message = null;
      this.subjectCommentsService.addThumbUp(this.universityId,this.subjectId,commentId)
          .then(result => {
              if(result){
                  this.fetch();
              }else{
                  this.message = "Unable to find comment, please refresh."
              }
          })
          .catch(e => {
              this.message = "Comments system has encountered an error, please refresh."
          });
  }

    thumbDown(commentId){
        this.message = null;
        this.subjectCommentsService.addThumbDown(this.universityId,this.subjectId,commentId)
            .then(result => {
                if(result){
                    this.fetch();
                }else{
                    this.message = "Unable to find comment, please refresh."
                }
            })
            .catch(e => {
                this.message = "Comments system has encountered an error, please refresh."
            });
    }

    flag(commentId){
        this.message = null;
        this.subjectCommentsService.flag(this.universityId,this.subjectId,commentId)
            .then(result => {
                if(result){
                    this.fetch();
                }else{
                    this.message = "Unable to find comment, please refresh."
                }
            })
            .catch(e => {
                this.message = "Comments system has encountered an error, please refresh."
            });
    }

    unflag(commentId){
        this.message = null;
        this.subjectCommentsService.unFlag(this.universityId,this.subjectId,commentId)
            .then(result => {
                if(result){
                    this.fetch();
                }else{
                    this.message = "Unable to find comment, please refresh."
                }
            })
            .catch(e => {
                this.message = "Comments system has encountered an error, please refresh."
            });
    }

    addNew(message){
        this.message = null;
        //need auth? or is spring enough
        if(this.canAddComment){
            this.subjectCommentsService.add(this.universityId,this.subjectId, message)
                .then(result => {
                    if(result){
                        this.fetch();
                    }else{
                        this.message = "Unable to find subject, please refresh."
                    }
                })
                .catch(e => {
                    this.message = "Comments system has encountered an error, please refresh or login."
                });
        }
    }

}
