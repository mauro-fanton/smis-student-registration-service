package actors

import akka.actor.{OneForOneStrategy, Props, SupervisorStrategy}
import akka.pattern.{BackoffOpts, BackoffSupervisor}

import scala.concurrent.duration.DurationInt

object SupervisorActor {

  /* BackoffSupervisor
      Start a child actor again when it fails. You can specify a min and max backof time which
      gives to the actor some time to restart. For example in  case that some resource stop
      working and need to restart with teh actor

      The following Scala snippet shows how to create a backoff supervisor which will start the given
      StudentManager actor after it has crashed because of some exception, in increasing intervals
      of 3, 6, 12, 24 and finally 30 seconds:
   */

  def createSupervisorWithOnFailureStrategy(childProps: Props): Props = {
    BackoffSupervisor.props(
      BackoffOpts.onFailure(
        childProps,
        childName = StudentManager.Name,
        minBackoff = 3.seconds,
        maxBackoff = 30.seconds,
        randomFactor = 0.2 // adds 20% "noise" to vary the intervals slightly
//      ).withSupervisorStrategy(OneForOneStrategy() {
//        _ => SupervisorStrategy.defaultDecider
//      })
    ))
  }

    def createSupervisorWithOnStopStrategy(childProps: Props): Props = {
      BackoffSupervisor.props(
        BackoffOpts.onStop(
          childProps,
          childName = StudentManager.Name,
          minBackoff = 3.seconds,
          maxBackoff = 30.seconds,
          randomFactor = 0.2 // adds 20% "noise" to vary the intervals slightly
        )
      )
  }
}
