package actors

import akka.actor.{OneForOneStrategy, Props, SupervisorStrategy}
import akka.pattern.{BackoffOpts, BackoffSupervisor}

import scala.concurrent.duration.DurationInt

object SupervisorActor {

  def createSupervisorWithOnFailureStrategy(childProps: Props): Props = {
    BackoffSupervisor.props(
      BackoffOpts.onFailure(
        childProps,
        childName = StudentManager.Name,
        minBackoff = 3.seconds,
        maxBackoff = 30.seconds,
        randomFactor = 0.2 // adds 20% "noise" to vary the intervals slightly
      ).withSupervisorStrategy(OneForOneStrategy() {
        _ => SupervisorStrategy.Restart
      })
    )
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
