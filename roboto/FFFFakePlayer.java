package dre.elfocrash.roboto;

import java.util.logging.Level;

import dre.elfocrash.roboto.ai.FFFFakePlayerAI;
import dre.elfocrash.roboto.helpers.FFFFakeHelpers;
import dre.elfocrash.roboto.helpers.FFFFakePMResponder;
import net.sf.l2j.commons.concurrent.ThreadPool;

import net.sf.l2j.gameserver.data.SkillTable.FrequentSkill;
import net.sf.l2j.gameserver.data.manager.CursedWeaponManager;
import net.sf.l2j.gameserver.data.xml.AdminData;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.instancemanager.SevenSigns;
import net.sf.l2j.gameserver.instancemanager.SevenSigns.CabalType;
import net.sf.l2j.gameserver.instancemanager.SevenSigns.SealType;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillTargetType;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Attackable;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.actor.appearance.PcAppearance;
import net.sf.l2j.gameserver.model.actor.instance.Door;
import net.sf.l2j.gameserver.model.actor.instance.Monster;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.model.actor.instance.StaticObject;
import net.sf.l2j.gameserver.model.actor.template.PlayerTemplate;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.model.entity.Siege.SiegeSide;
import net.sf.l2j.gameserver.model.group.Party.MessageType;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.olympiad.OlympiadManager;
import net.sf.l2j.gameserver.model.pledge.ClanMember;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowMemberListUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.l2skills.L2SkillSiegeFlag;
import net.sf.l2j.gameserver.templates.skills.L2SkillType;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Elfocrash
 *
 */
public class FFFFakePlayer extends Player
{
    // Shared responder instance
    private static final FFFFakePMResponder _pmResponder = new FFFFakePMResponder();

    /**
     * Called when this bot receives a private message.
     * Stores last sender and message, then responds.
     */
    public void onPrivateMessageReceived(String senderName, String messageText) {
        setLastPrivateMessage(senderName, messageText);
        _pmResponder.handleIncomingPM(this, senderName, messageText);
    }

    private FFFFakePlayerAI _ffffakeAi;
    private boolean _underControl = false;
    private String _lastPMText = "";
    private String _lastPMFrom = "";
    private boolean _pvpEnabled = false;  // <--- Εδώ είναι η μεταβλητή για το pvp enabled
    private Location _spawnLocation;
    private ScheduledFuture<?> _aiTask;
    
    public void sendPrivateMessage(String receiverName, String messageText) {
        Player receiver = World.getInstance().getPlayer(receiverName);

        if (receiver == null || receiver.equals(this)) {
            return;
        }
        receiver.sendPacket(new CreatureSay(getObjectId(), 2, getName(), messageText));
        sendPacket(new CreatureSay(getObjectId(), 2, "->" + receiver.getName(), messageText));
    }

    public boolean isUnderControl() {
        return _underControl;
    }

    public void setUnderCCCControl(boolean underControl) {
        _underControl = underControl;
    }

    protected FFFFakePlayer(int objectId) {
        super(objectId);
    }

    public FFFFakePlayer(int objectId, PlayerTemplate template, String accountName, PcAppearance app) {
        super(objectId, template, accountName, app);
    }

    public FFFFakePlayerAI getFFFFakeAi() {
        return _ffffakeAi;
    }

    public void setFFFFakeAi(FFFFakePlayerAI _ffffakeAi) {
        this._ffffakeAi = _ffffakeAi;
    }

    public void assignDefaultAI() {
        try {
            setFFFFakeAi(FFFFakeHelpers.getAIbyClassId(getClassId()).getConstructor(FFFFakePlayer.class).newInstance(this));
            Location loc = this.getPosition();
            this.setSpawnLocation(new Location(loc.getX(), loc.getY(), loc.getZ()));
            this.startAITask();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLastPrivateMessage(String senderName, String messageText) {
        _lastPMFrom = senderName;
        _lastPMText = messageText;
    }

    public String getLastPMFrom() {
        return _lastPMFrom;
    }

    public String getLastPMText() {
        return _lastPMText;
    }

    public boolean checkUseMagicConditions(L2Skill skill, boolean forceUse, boolean dontMove) {
        if (skill == null)
            return false;

        if (isDead() || isOutOfControl()) {
            sendPacket(ActionFailed.STATIC_PACKET);
            return false;
        }

        if (isSkillDisabled(skill))
            return false;

        L2SkillType sklType = skill.getSkillType();

        if (isFishing() && (sklType != L2SkillType.PUMPING && sklType != L2SkillType.REELING && sklType != L2SkillType.FISHING)) {
            return false;
        }

        if (isInObserverMode()) {
            abortCast();
            return false;
        }

        if (isSitting()) {
            if (skill.isToggle()) {
                L2Effect effect = getFirstEffect(skill.getId());
                if (effect != null) {
                    effect.exit();
                    return false;
                }
            }
            return false;
        }

        if (skill.isToggle()) {
            L2Effect effect = getFirstEffect(skill.getId());

            if (effect != null) {
                if (skill.getId() != 60)
                    effect.exit();

                sendPacket(ActionFailed.STATIC_PACKET);
                return false;
            }
        }

        if (isFakeDeath()) {
            sendPacket(ActionFailed.STATIC_PACKET);
            return false;
        }

        WorldObject target = null;
        SkillTargetType sklTargetType = skill.getTargetType();
        Location worldPosition = getCurrentSkillWorldPosition();

        if (sklTargetType == SkillTargetType.TARGET_GROUND && worldPosition == null) {
            _log.info("WorldPosition is null for skill: " + skill.getName() + ", player: " + getName() + ".");
            sendPacket(ActionFailed.STATIC_PACKET);
            return false;
        }

        switch (sklTargetType) {
        // Target the player if skill type is AURA, PARTY, CLAN or SELF
        case TARGET_AURA:
        case TARGET_FRONT_AURA:
        case TARGET_BEHIND_AURA:
        case TARGET_AURA_UNDEAD:
        case TARGET_PARTY:
        case TARGET_ALLY:
        case TARGET_CLAN:
        case TARGET_GROUND:
        case TARGET_SELF:
        case TARGET_CORPSE_ALLY:
        case TARGET_AREA_SUMMON:
            target = this;
            break;
        case TARGET_PET:
        case TARGET_SUMMON:
            target = getPet();
            break;
        default:
            target = getTarget();
            break;
        }

        // Check the validity of the target
        if (target == null) {
            sendPacket(ActionFailed.STATIC_PACKET);
            return false;
        }

        if (target instanceof Door) {
            if (!((Door) target).isAutoAttackable(this) // Siege doors only hittable during siege
                    || (((Door) target).isUnlockable() && skill.getSkillType() != L2SkillType.UNLOCK)) // unlockable doors
            {
                sendPacket(SystemMessageId.INCORRECT_TARGET);
                sendPacket(ActionFailed.STATIC_PACKET);
                return false;
            }
        }

        // Are the target and the player in the same duel?
        if (isInDuel()) {
            if (target instanceof Playable) {
                // Get Player
                Player cha = target.getActingPlayer();
                if (cha.getDuelId() != getDuelId()) {
                    sendPacket(SystemMessageId.INCORRECT_TARGET);
                    sendPacket(ActionFailed.STATIC_PACKET);
                    return false;
                }
            }
        }

        // ************************************* Check skill availability *******************************************

        // Siege summon checks. Both checks send a message to the player if it return false.
        if (skill.isSiegeSummonSkill()) {
            final Siege siege = CastleManager.getInstance().getActiveSiege(this);
            if (siege == null || !siege.checkSide(getClan(), SiegeSide.ATTACKER) || (isInSiege() && isInsideZone(ZoneId.CASTLE))) {
                sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_CALL_PET_FROM_THIS_LOCATION));
                return false;
            }

            if (SevenSigns.getInstance().isSealValidationPeriod()
                    && SevenSigns.getInstance().getSealOwner(SealType.STRIFE) == CabalType.DAWN
                    && SevenSigns.getInstance().getPlayerCabal(getObjectId()) == CabalType.DUSK) {
                sendPacket(SystemMessageId.SEAL_OF_STRIFE_FORBIDS_SUMMONING);
                return false;
            }
        }

        // ************************************* Check casting conditions *******************************************

        // Check if all casting conditions are completed
        if (!skill.checkCondition(this, target, false)) {
            // Send ActionFailed to the Player
            sendPacket(ActionFailed.STATIC_PACKET);
            return false;
        }

        // ************************************* Check Skill Type *******************************************

        // Check if this is offensive magic skill
        if (skill.isOffensive()) {
            if (isInsidePeaceZone(this, target)) {
                // If Creature or target is in a peace zone, send a system message TARGET_IN_PEACEZONE ActionFailed
                sendPacket(SystemMessageId.TARGET_IN_PEACEZONE);
                sendPacket(ActionFailed.STATIC_PACKET);
                return false;
            }

            if (isInOlympiadMode() && !isOlympiadStart()) {
                // if Player is in Olympia and the match isn't already start, send ActionFailed
                sendPacket(ActionFailed.STATIC_PACKET);
                return false;
            }

            // Check if the target is attackable
            if (!target.isAttackable() && !getAccessLevel().allowPeaceAttack()) {
                // If target is not attackable, send ActionFailed
                sendPacket(ActionFailed.STATIC_PACKET);
                return false;
            }

            // Check if a Forced ATTACK is in progress on non-attackable target
            if (!target.isAutoAttackable(this) && !forceUse) {
                switch (sklTargetType) {
                case TARGET_AURA:
                case TARGET_FRONT_AURA:
                case TARGET_BEHIND_AURA:
                case TARGET_AURA_UNDEAD:
                case TARGET_CLAN:
                case TARGET_ALLY:
                case TARGET_PARTY:
                case TARGET_SELF:
                case TARGET_GROUND:
                case TARGET_CORPSE_ALLY:
                case TARGET_AREA_SUMMON:
                    break;
                default: // Send ActionFailed to the Player
                    sendPacket(ActionFailed.STATIC_PACKET);
                    return false;
                }
            }

            // Check if the target is in the skill cast range
            if (dontMove) {
                // Calculate the distance between the Player and the target
                if (sklTargetType == SkillTargetType.TARGET_GROUND) {
                    if (!isInsideRadius(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(),
                            (int) (skill.getCastRange() + getCollisionRadius()), false, false)) {
                        // Send a System Message to the caster
                        sendPacket(SystemMessageId.TARGET_TOO_FAR);

                        // Send ActionFailed to the Player
                        sendPacket(ActionFailed.STATIC_PACKET);
                        return false;
                    }
                } else if (skill.getCastRange() > 0
                        && !isInsideRadius(target, (int) (skill.getCastRange() + getCollisionRadius()), false, false)) {
                    // Send a System Message to the caster
                    sendPacket(SystemMessageId.TARGET_TOO_FAR);

                    // Send ActionFailed to the Player
                    sendPacket(ActionFailed.STATIC_PACKET);
                    return false;
                }
            }
        }

        // Check if the skill is defensive
        if (!skill.isOffensive() && target instanceof Monster && !forceUse) {
            // check if the target is a monster and if force attack is set.. if not then we don't want to cast.
            switch (sklTargetType) {
            case TARGET_PET:
            case TARGET_SUMMON:
            case TARGET_AURA:
            case TARGET_FRONT_AURA:
            case TARGET_BEHIND_AURA:
            case TARGET_AURA_UNDEAD:
            case TARGET_CLAN:
            case TARGET_SELF:
            case TARGET_CORPSE_ALLY:
            case TARGET_PARTY:
            case TARGET_ALLY:
            case TARGET_CORPSE_MOB:
            case TARGET_AREA_CORPSE_MOB:
            case TARGET_GROUND:
                break;
            default: {
                switch (sklType) {
                case BEAST_FEED:
                case DELUXE_KEY_UNLOCK:
                case UNLOCK:
                    break;
                default:
                    sendPacket(ActionFailed.STATIC_PACKET);
                    return false;
                }
                break;
            }
            }
        }

        // Check if the skill is Spoil type and if the target isn't already spoiled
        if (sklType == L2SkillType.SPOIL) {
            if (!(target instanceof Monster)) {
                // Send a System Message to the Player
                sendPacket(SystemMessageId.INCORRECT_TARGET);

                // Send ActionFailed to the Player
                sendPacket(ActionFailed.STATIC_PACKET);
                return false;
            }
        }

        // Check if the skill is Sweep type and if conditions not apply
        if (sklType == L2SkillType.SWEEP && target instanceof Attackable) {
            if (((Attackable) target).isDead()) {
                final int spoilerId = ((Attackable) target).getSpoilerId();
                if (spoilerId == 0) {
                    // Send a System Message to the Player
                    sendPacket(SystemMessageId.SWEEPER_FAILED_TARGET_NOT_SPOILED);

                    // Send ActionFailed to the Player
                    sendPacket(ActionFailed.STATIC_PACKET);
                    return false;
                }

                if (!isLooterOrInLooterParty(spoilerId)) {
                    // Send a System Message to the Player
                    sendPacket(SystemMessageId.SWEEP_NOT_ALLOWED);

                    // Send ActionFailed to the Player
                    sendPacket(ActionFailed.STATIC_PACKET);
                    return false;
                }
            }
        }

        // Check if the skill is Drain Soul (Soul Crystals) and if the target is a MOB
        if (sklType == L2SkillType.DRAIN_SOUL) {
            if (!(target instanceof Monster)) {
                // Send a System Message to the Player
                sendPacket(SystemMessageId.INCORRECT_TARGET);

                // Send ActionFailed to the Player
                sendPacket(ActionFailed.STATIC_PACKET);
                return false;
            }
        }

        // Check if this is a Pvp skill and target isn't a non-flagged/non-karma player
        switch (sklTargetType) {
        case TARGET_PARTY:
        case TARGET_ALLY: // For such skills, checkPvpSkill() is called from L2Skill.getTargetList()
        case TARGET_CLAN: // For such skills, checkPvpSkill() is called from L2Skill.getTargetList()
        case TARGET_AURA:
        case TARGET_FRONT_AURA:
        case TARGET_BEHIND_AURA:
        case TARGET_AURA_UNDEAD:
        case TARGET_GROUND:
        case TARGET_SELF:
        case TARGET_CORPSE_ALLY:
        case TARGET_AREA_SUMMON:
            break;
        default:
            if (!checkPvpSkill(target, skill) && !getAccessLevel().allowPeaceAttack()) {
                // Send a System Message to the Player
                sendPacket(SystemMessageId.TARGET_IS_INCORRECT);

                // Send ActionFailed to the Player
                sendPacket(ActionFailed.STATIC_PACKET);
                return false;
            }
        }

        if ((sklTargetType == SkillTargetType.TARGET_ONE 
             || sklTargetType == SkillTargetType.TARGET_AREA 
             || sklTargetType == SkillTargetType.TARGET_FRONT_AREA
             || sklTargetType == SkillTargetType.TARGET_BEHIND_AREA)
            && !GeoEngine.getInstance().canSeeTarget(this, target)) {
            sendPacket(ActionFailed.STATIC_PACKET);
            return false;
        }

        return true;
    }
    
    public Location getSpawnLocation() {
        return _spawnLocation;
    }

    public void setSpawnLocation(Location loc) {
        _spawnLocation = loc;
    }

    public void startAITask() {
        if (_aiTask != null && !_aiTask.isDone()) {
            _aiTask.cancel(false);
        }
        _aiTask = ThreadPool.scheduleAtFixedRate(() -> {
            if (_ffffakeAi != null && !isDead()) {
                _ffffakeAi.thinkAndAct();
            }
        }, 1000, 1000);
    }

    public void despawnPlayer() {
        if (getParty() != null) {
            getParty().removePartyMember(this, MessageType.EXPELLED);
        }

        if (getClan() != null) {
            getClan().removeClanMember(getObjectId(), 0);
            getClan().broadcastToOnlineMembers(new PledgeShowMemberListUpdate(this));
        }

                FFFFakePlayerManager.INSTANCE.removeActiveFakePlayer(this);
                FFFFakePlayerTaskManager.INSTANCE.removeFromTasks(this);       
        
        // Remove the player from the world
        World.getInstance().removeObject(this);

        // Stop AI
        if (_ffffakeAi != null) {
            stopAITask();
            _ffffakeAi = null;
        }
    }
    
    public void stopAITask() {
        if (_aiTask != null) {
            _aiTask.cancel(false);
            _aiTask = null;
        }
    }

    /**
     * Heal to full HP/MP/CP
     */
    public void heal() {
        setCurrentHp(getMaxHp());
        setCurrentMp(getMaxMp());
        setCurrentCp(getMaxCp());
    }

    // Εδώ προσθέτουμε τις μεθόδους που ζήτησες:

    public boolean isPvpEnabled() {
        return _pvpEnabled;
    }

    public void setPvpEnabled(boolean pvpEnabled) {
        _pvpEnabled = pvpEnabled;
    }

    public void forceAutoAttack(Creature target) {
        if (target == null || isDead() || isOutOfControl())
            return;

        // Αν δεν είναι ήδη target, στοχεύουμε
        if (getTarget() != target) {
            setTarget(target);
        }

        // Αν δεν επιτίθεται ήδη, ξεκινάμε auto attack
        if (!isAttackingNow()) {
            getAI().setIntention(CtrlIntention.ATTACK, target);
        }
    }
    
    /**
     * Ελέγχει αν ο παίκτης είναι εντός ακτίνας από μία τοποθεσία.
     *
     * @param loc Η τοποθεσία αναφοράς
     * @param radius Η απόσταση ακτίνας
     * @param checkZ Αν θα ελέγξει και στον άξονα Z (ύψος)
     * @param strictCheck Αν θα είναι αυστηρός ο έλεγχος (λιγότερο ή ίσο)
     * @return true αν είναι εντός ακτίνας, αλλιώς false
     */
    public boolean isInsideRadius(Location loc, int radius, boolean checkZ, boolean strictCheck) {
        if (loc == null)
            return false;

        int dx = getX() - loc.getX();
        int dy = getY() - loc.getY();
        int dz = getZ() - loc.getZ();

        if (checkZ && Math.abs(dz) > radius)
            return false;

        double distSq = dx * dx + dy * dy;

        if (strictCheck)
            return distSq < radius * radius;
        else
            return distSq <= radius * radius;
    }
}
