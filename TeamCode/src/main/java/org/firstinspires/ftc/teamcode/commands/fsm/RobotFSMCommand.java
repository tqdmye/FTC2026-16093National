package org.firstinspires.ftc.teamcode.commands.fsm;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Subsystems.BallStorage;
import org.firstinspires.ftc.teamcode.Subsystems.Firer;
import org.firstinspires.ftc.teamcode.Subsystems.IntakePreshooter;
import org.firstinspires.ftc.teamcode.Subsystems.Led;
import org.firstinspires.ftc.teamcode.Subsystems.shooter.ShooterFSM;

import java.util.function.BooleanSupplier;

public class RobotFSMCommand extends CommandBase {

    public enum RobotMode {
        SAFE,
        COMBAT;
    }

    private RobotMode robotMode = RobotMode.SAFE;

    private final ShooterFSM shooterFSM;
    private final Firer firer;
    private final Led led;
    private final BallStorage ballStorage;

    private final BooleanSupplier fireRequest;
    private final BooleanSupplier modeToggle;

    public RobotFSMCommand(
            ShooterFSM shooterFSM,
            Firer intake,
            Led led,
            BallStorage ballStorage,
            BooleanSupplier fireRequest,
            BooleanSupplier modeToggle
    ) {
        this.shooterFSM = shooterFSM;
        this.firer = intake;
        this.led = led;
        this.ballStorage = ballStorage;
        this.fireRequest = fireRequest;
        this.modeToggle = modeToggle;
    }

    @Override
    public void execute() {

        /* ---------- Mode 切换 ---------- */
        if (modeToggle.getAsBoolean()) {
            robotMode = (robotMode == RobotMode.SAFE)
                    ? RobotMode.COMBAT
                    : RobotMode.SAFE;
        }

        /* ---------- SAFE ---------- */
        if (robotMode == RobotMode.SAFE) {
            shooterFSM.accelerate_idle();
            firer.dntFire();
            led.setNone();
            return;
        }

        /* ---------- COMBAT ---------- */
        if (robotMode == RobotMode.COMBAT){
            if(shooterFSM.state == ShooterFSM.State.READY){
                led.setGreen();
            }
            else{
                led.setBlue();
            }
            if(fireRequest.getAsBoolean()){
                firer.shoot();
            }
            if(shooterFSM.state == ShooterFSM.State.FAST){
                shooterFSM.accelerate_fast();
            }
            else{

            }
        }

        /* Ball Full Event */
        if (ballStorage.isFull()) {
            led.setBallFull();
        }
    }

    public RobotMode getRobotMode() {
        return robotMode;
    }
}
